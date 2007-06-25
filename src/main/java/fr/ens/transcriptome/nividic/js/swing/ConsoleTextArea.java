/*
 *                      Nividic development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the microarray platform
 * of the École Normale Supérieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.js.swing;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import javax.swing.text.Segment;

class ConsoleWrite implements Runnable {
  private ConsoleTextArea textArea;
  private String str;

  public ConsoleWrite(ConsoleTextArea textArea, String str) {
    this.textArea = textArea;
    this.str = str;

  }

  public void run() {

    // textArea.setForeground(color);
    textArea.write(str);
  }
};

class ConsoleWriter extends java.io.OutputStream {

  private ConsoleTextArea textArea;
  private StringBuffer buffer;
  private Color color;

  public ConsoleWriter(ConsoleTextArea textArea, Color color) {
    this.textArea = textArea;
    buffer = new StringBuffer();
    this.color = color;
  }

  public synchronized void write(int ch) {
    buffer.append((char) ch);
    if (ch == '\n') {
      flushBuffer();
    }
  }

  public synchronized void write(char[] data, int off, int len) {
    for (int i = off; i < len; i++) {
      buffer.append(data[i]);
      if (data[i] == '\n') {
        flushBuffer();
      }
    }
  }

  public synchronized void flush() {
    if (buffer.length() > 0) {
      flushBuffer();
    }
  }

  public void close() {
    flush();
  }

  private void flushBuffer() {
    String str = buffer.toString();
    buffer.setLength(0);
    SwingUtilities.invokeLater(new ConsoleWrite(textArea, str));
  }
};

public class ConsoleTextArea extends JTextArea implements KeyListener,
    DocumentListener {
  static final long serialVersionUID = 8557083244830872961L;

  private transient ConsoleWriter console1;
  private transient ConsoleWriter console2;
  private transient PrintStream out;
  private transient PrintStream err;
  private transient PrintWriter inPipe;
  private transient PipedInputStream in;
  private transient java.util.Vector history;
  private int historyIndex = -1;
  private int outputMark = 0;

  public void select(int start, int end) {
    requestFocus();
    super.select(start, end);
  }

  public ConsoleTextArea(String[] argv) {
    super();
    history = new java.util.Vector();
    console1 = new ConsoleWriter(this, Color.black);
    console2 = new ConsoleWriter(this, Color.red);
    out = new PrintStream(console1);
    err = new PrintStream(console2);
    PipedOutputStream outPipe = new PipedOutputStream();
    inPipe = new PrintWriter(outPipe);
    in = new PipedInputStream();
    try {
      outPipe.connect(in);
    } catch (IOException exc) {
      exc.printStackTrace();
    }
    getDocument().addDocumentListener(this);
    addKeyListener(this);
    setLineWrap(true);
    setFont(new Font("Monospaced", 0, 12));
  }

  synchronized void returnPressed() {
    Document doc = getDocument();
    int len = doc.getLength();
    Segment segment = new Segment();
    try {
      doc.getText(outputMark, len - outputMark, segment);
    } catch (javax.swing.text.BadLocationException ignored) {
      ignored.printStackTrace();
    }
    if (segment.count > 0) {
      history.addElement(segment.toString());
    }
    historyIndex = history.size();
    inPipe.write(segment.array, segment.offset, segment.count);
    append("\n");
    outputMark = doc.getLength();
    inPipe.write("\n");
    inPipe.flush();
    console1.flush();
  }

  public void eval(String str) {
    inPipe.write(str);
    inPipe.write("\n");
    inPipe.flush();
    console1.flush();
  }

  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_BACK_SPACE || code == KeyEvent.VK_LEFT) {
      if (outputMark == getCaretPosition()) {
        e.consume();
      }
    } else if (code == KeyEvent.VK_HOME) {
      int caretPos = getCaretPosition();
      if (caretPos == outputMark) {
        e.consume();
      } else if (caretPos > outputMark) {
        if (!e.isControlDown()) {
          if (e.isShiftDown()) {
            moveCaretPosition(outputMark);
          } else {
            setCaretPosition(outputMark);
          }
          e.consume();
        }
      }
    } else if (code == KeyEvent.VK_ENTER) {
      returnPressed();
      e.consume();
    } else if (code == KeyEvent.VK_UP) {
      historyIndex--;
      if (historyIndex >= 0) {
        if (historyIndex >= history.size()) {
          historyIndex = history.size() - 1;
        }
        if (historyIndex >= 0) {
          String str = (String) history.elementAt(historyIndex);
          int len = getDocument().getLength();
          replaceRange(str, outputMark, len);
          int caretPos = outputMark + str.length();
          select(caretPos, caretPos);
        } else {
          historyIndex++;
        }
      } else {
        historyIndex++;
      }
      e.consume();
    } else if (code == KeyEvent.VK_DOWN) {
      int caretPos = outputMark;
      if (history.size() > 0) {
        historyIndex++;
        if (historyIndex < 0) {
          historyIndex = 0;
        }
        int len = getDocument().getLength();
        if (historyIndex < history.size()) {
          String str = (String) history.elementAt(historyIndex);
          replaceRange(str, outputMark, len);
          caretPos = outputMark + str.length();
        } else {
          historyIndex = history.size();
          replaceRange("", outputMark, len);
        }
      }
      select(caretPos, caretPos);
      e.consume();
    }
  }

  public void keyTyped(KeyEvent e) {
    int keyChar = e.getKeyChar();
    if (keyChar == 0x8 /* KeyEvent.VK_BACK_SPACE */) {
      if (outputMark == getCaretPosition()) {
        e.consume();
      }
    } else if (getCaretPosition() < outputMark) {
      setCaretPosition(outputMark);
    }
  }

  public synchronized void keyReleased(KeyEvent e) {
  }

  public synchronized void write(String str) {
    insert(str, outputMark);
    int len = str.length();
    outputMark += len;
    select(outputMark, outputMark);
  }

  public synchronized void insertUpdate(DocumentEvent e) {
    int len = e.getLength();
    int off = e.getOffset();
    if (outputMark > off) {
      outputMark += len;
    }
  }

  public synchronized void removeUpdate(DocumentEvent e) {
    int len = e.getLength();
    int off = e.getOffset();
    if (outputMark > off) {
      if (outputMark >= off + len) {
        outputMark -= len;
      } else {
        outputMark = off;
      }
    }
  }

  public synchronized void postUpdateUI() {
    // this attempts to cleanup the damage done by updateComponentTreeUI
    requestFocus();
    setCaret(getCaret());
    select(outputMark, outputMark);
  }

  public synchronized void changedUpdate(DocumentEvent e) {
  }

  public InputStream getIn() {
    return in;
  }

  public PrintStream getOut() {
    return out;
  }

  public PrintStream getErr() {
    return err;
  }

};
