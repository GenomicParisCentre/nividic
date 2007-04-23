/****************************************************************************
 **
 ** Copyright (C) 1992-2007 Trolltech ASA. All rights reserved.
 **
 ** This file is part of Qt Jambi.
 **
 ** $JAVA_LICENSE$
 **
 ** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 ** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 **
 ****************************************************************************/

package fr.ens.transcriptome.nividic.js.swing;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

public class AnalogClock extends QWidget {
  static QPolygon hourHand = new QPolygon();
  static QPolygon minuteHand = new QPolygon();
  static {
    hourHand.append(new QPoint(7, 8));
    hourHand.append(new QPoint(-7, 8));
    hourHand.append(new QPoint(0, -40));

    minuteHand.append(new QPoint(7, 8));
    minuteHand.append(new QPoint(-7, 8));
    minuteHand.append(new QPoint(0, -70));
  }

  QTimer m_timer = new QTimer(this);

  public AnalogClock() {
    this(null);
  }

  public AnalogClock(QWidget parent) {
    super(parent);
    m_timer.timeout.connect(this, "update()");

    setWindowTitle("Analog clock");
    setWindowIcon(new QIcon("classpath:com/trolltech/images/qt-logo.png"));
    resize(200, 200);
  }

  protected void paintEvent(QPaintEvent e) {
    QColor hourColor = new QColor(127, 0, 127);
    QColor minuteColor = new QColor(0, 127, 127, 191);

    int side = width() < height() ? width() : height();

    QTime time = QTime.currentTime();

    QPainter painter = new QPainter(this);
    painter.setRenderHint(QPainter.RenderHint.Antialiasing);
    painter.translate(width() / 2, height() / 2);
    painter.scale(side / 200.0f, side / 200.0f);

    painter.setPen(QPen.NoPen);
    painter.setBrush(hourColor);

    painter.save();
    painter.rotate(30.0f * ((time.hour() + time.minute() / 60.0f)));
    painter.drawConvexPolygon(hourHand);
    painter.restore();

    painter.setPen(hourColor);

    for (int i = 0; i < 12; ++i) {
      painter.drawLine(88, 0, 96, 0);
      painter.rotate(30.0f);
    }

    painter.setPen(QPen.NoPen);
    painter.setBrush(minuteColor);

    painter.save();
    painter.rotate(6.0f * (time.minute() + time.second() / 60.0f));
    painter.drawConvexPolygon(minuteHand);
    painter.restore();

    painter.setPen(minuteColor);

    for (int j = 0; j < 60; ++j) {
      if ((j % 5) != 0)
        painter.drawLine(92, 0, 96, 0);
      painter.rotate(6.0f);
    }
  }

  public QSize sizeHint() {
    return new QSize(200, 200);
  }

  public void showEvent(QShowEvent e) {
    m_timer.start(1000);
  }

  public void hideEvent(QHideEvent e) {
    m_timer.stop();
  }

  static public void main(String args[]) {
    QApplication.initialize(args);
    AnalogClock w = new AnalogClock();

    w.show();

    QApplication.exec();
  }
}
