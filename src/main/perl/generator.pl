#/usr/bin/perl -w
use strict;

# TODO generate ressource files
# TODO add array types, Filters, annotation

my $class_name = "SetBiologicalObjectName";
my $object_type = "BioAssay";
my $method = "void setName(String name, BioAssay bioAssay);";
my $description = "This wrapper set the name of a BioAssay";

my $package = "fr.ens.transcriptome.nividic.taverna.processors.generated";

sub trim($) {
  my ($ch) = @_;
  if (!defined($ch)) { return undef; }
  $ch =~ s/^\s*//;
  $ch =~ s/\s*$//;
  $ch;
}



sub to_lower_case($) {
  my ($ch) = @_;
  if (!defined($ch)) { return undef; }
  $_ = $ch;
  s/(.*)/\L$1/;
  return $_;
}

sub first_to_lower_case($) {
  my ($ch) = @_;
  if (!defined($ch)) { return undef; }
  $_ = $ch;
  s/^(.)/\L$1/;
  return $_;
}

sub first_to_upper_case($) {
  my ($ch) = @_;
  if (!defined($ch)) { return undef; }
  $_ = $ch;
  s/^(.)/\U$1/;
  return $_;
}


sub getMineType($) {

	my ($type) = @_;

	my $new_type;

	if ($type eq "String") { $new_type = "STRING"; }
	elsif ($type eq "int") { $new_type = "INT"; }
	elsif ($type eq "double") { $new_type = "DOUBLE"; }
	elsif ($type eq "boolean") { $new_type = "BOOLEAN"; }
	elsif ($type eq "String[]" or $type eq "List<String>") { $new_type = "ListOfSTRING"; }
	elsif ($type eq "int[]" or $type eq "List<Integer>" ) { $new_type = "ListOfINT"; }
	elsif ($type eq "double[]" or $type eq "List<Double>") { $new_type = "ListOfDOUBLE"; }
	elsif ($type eq "boolean[]" or $type eq "List<Boolean>") { $new_type = "ListOfBOOLEAN"; }
	elsif ($type eq "Annotation") { $new_type = "ANNOTATION"; }
	elsif ($type eq "BioAssay") { $new_type = "BIOASSAY"; }
	elsif ($type eq "BioAssay[]" or $type eq "List<BioAssay>") { $new_type = "ListOfBIOASSAY"; }
	elsif ($type eq "BioAssayFilter") { $new_type = "BIOASSAY_FILTER"; }
	elsif ($type eq "ExpressionMatrix") { $new_type = "MATRIX"; }
	elsif ($type eq "ExpressionMatrix[]" or $type eq "List<ExpressionMatrix>") { $new_type = "ListOfMATRIX"; }
	elsif ($type eq "ExpressionMatrixFilter") { $new_type = "MATRIX_FILTER"; }
	elsif ($type eq "ExpressionMatrixDimension") { $new_type = "DIMENSION"; }
	elsif ($type eq "ExpressionMatrixDimension[]" or $type eq "List<ExpressionMatrixDimension>") { $new_type = "ListOfDIMENSION"; }
	elsif ($type eq "BiologicalList") { $new_type = "BIOLIST"; }
	elsif ($type eq "BiologicalListFilter") { $new_type = "BIOLIST_FILTER"; }
	elsif ($type eq "BiologicalList[]" or $type eq "List<BiologicalList>") { $new_type = "ListOfBiologicalList"; }
	elsif ($type eq "Design") { $new_type = "DESIGN"; }
	elsif ($type eq "Translator") { $new_type = "TRANSLATOR"; }
	else { print "getMimeType: Unknown type: $type\n"; exit 0; }

	return $new_type;
}

sub generate_getter_code {

	my ($name, $type) = @_;

	my $primitive_type = "";

	my $result;

	$result .= "\t\t\tfinal $type $name;\n";
	$result .= "\t\t\ttry {\n";
	$result .= "\t\t\t\t$name = ndta.get";

	if ($type eq "String") { $result .="String"; }
	elsif ($type eq "boolean") { $result .="Boolean"; }
	elsif ($type eq "byte") { $result .="Byte"; $primitive_type = 1; }
	elsif ($type eq "short") { $result .="Short"; $primitive_type = 1; }
	elsif ($type eq "char") { $result .="Char"; $primitive_type = 1; }
	elsif ($type eq "int") { $result .="Int"; $primitive_type = 1; }
	elsif ($type eq "long") { $result .="Long"; $primitive_type = 1; }
	elsif ($type eq "float") { $result .="Float"; $primitive_type = 1; }
	elsif ($type eq "double") { $result .="Double"; $primitive_type = 1; }
	elsif ($type eq "String[]") { $result .="StringArray"; }
	elsif ($type eq "boolean[]") { $result .="BooleanArray"; }
	elsif ($type eq "byte[]") { $result .="ByteArray"; }
	elsif ($type eq "short[]") { $result .="ShortArray"; }
	elsif ($type eq "char[]") { $result .="CharArray"; }
	elsif ($type eq "int[]") { $result .="IntArray"; }
	elsif ($type eq "long[]") { $result .="LongArray"; }
	elsif ($type eq "float[]") { $result .="FloatArray"; }
	elsif ($type eq "double[]") { $result .="DoubleArray"; }
	elsif ($type eq "BiologicalObject") { $result .="BiologicalObject"; }
	elsif ($type eq "BioAssay") { $result .="BioAssay"; }
	elsif ($type eq "ExpressionMatrix") { $result .="ExpressionMatrix"; }
	elsif ($type eq "ExpressionMatrixDimension") { $result .="ExpressionMatrixDimension"; }
	elsif ($type eq "Design") { $result .="Design"; }
	elsif ($type eq "BiologicalList") { $result .="BiologicalList"; }
	elsif ($type eq "Annotation") { $result .="Annotation"; }
	elsif ($type eq "BioAssayFilter") { $result .="BioAssayFilter"; }
	elsif ($type eq "ExpressionMatrixFilter") { $result .="ExpressionMatrixFilter"; }
	elsif ($type eq "BiologicalListFilter") { $result .="BiologicalListFilter"; }
	elsif ($type eq "Translator") { $result .="Translator"; }

	else { print "generate_getter_code Unknown type: $type\n"; exit 0; }

	$result .= "(\"$name\");\n";
	$result .= "\t\t\t} catch (ClassCastException e) {\n" .
										"\t\t\t\tthrow new TaskExecutionException(\"Invalid type for $name.\");\n\t\t\t}";

	if ($primitive_type == 1) {

		$result .= " catch (NumberFormatException e) {\n" .
										"\t\t\t\tthrow new TaskExecutionException(\"Invalid number format for $name.\");\n\t\t\t}";

	}

	$result .= "\n";

	return $result;

}

sub createClassCode {

	my ($class_name, $object_type, $utility_class, $method, $description, $package) = @_;
	
	my $return_type = (split " ", $method)[0];
	my $funct;
	my $args;
	
	if ($method =~ /^\s*[\w\[\]\<\>]+\s+([\w\d]+)\(/) {
	
		$funct=$1;
	}
	
	if ($funct eq '') { print "Funct is null: $method\n"; exit 0; }

	if ($method =~ /\((.*)\)/) {
	
		$args=$1;
	}
	
	my @array_processor_input_types;
	my @array_processor_input_names;

	my @array_method_arguments = split ",", $args;
	
	
	#if (!($return_type eq 'void' && $utility_class ne '')) {
	if ($utility_class eq '') {
	
		push @array_processor_input_types, $object_type;
		push @array_processor_input_names, &first_to_lower_case($object_type);
	}

	foreach my $p (@array_method_arguments) {
	
		if ($p =~ /\s*([\w\d\[\]]+)+\s*([\w\d\[\]]+)\s*/) {
	
			my $type=$1;
			my $name = $2;
	
			push @array_processor_input_types, $type;
			push @array_processor_input_names, $name;
		}
	}	
	
	#
	# Create $input_names
	#
	
	my $input_names = "";
	my $first = 1;
	foreach my $name (@array_processor_input_names) {
	
		if ($first != 1) { $input_names .=",";}
		else { $first = 0;}
	
	
		$input_names .= "\"$name\"";
	}
	
	#
	# Create $input_types
	#
	
	my $input_types = "";
	my $first = 1;
	foreach my $type (@array_processor_input_types) {
	
		if (!($type eq "void")) {

			if ($first != 1) { $input_types .=",";}
			else { $first = 0;}
			my $new_type = getMineType($type);
	
			$input_types .= $new_type;
		}
	}
	
	#
	# Create output_names
	#
	
	my $output_names;
	my $output_types;

	if ($return_type eq "void") {

		$output_names = "\"" . &first_to_lower_case($object_type) . "\"";
		$output_types = getMineType($object_type);
	}
	else {

		$output_names = "\"output\"";
		$output_types = getMineType($return_type);
	}
	
	#
	# Create imports
	#
	
	my %hash_imports;

	my @all_types;

	push @all_types, @array_processor_input_types;
	if ($return_type ne "void") { push @all_types, $return_type; }
	if ($object_type ne "") { push @all_types, $object_type; }
	if ($utility_class ne "") { push @all_types, $utility_class; }
	

	foreach my $type (@all_types) {

		if ($type eq "String" || $type eq "int" || $type eq "double" || $type eq "boolean") {}
		elsif ($type eq "String[]" || $type eq "int[]" || $type eq "double[]" || $type eq "boolean[]") {}
		elsif ($type eq "BioAssay")						{ $hash_imports{"fr.ens.transcriptome.nividic.om.BioAssay"} = 1; }
		elsif ($type eq "BioAssayFilter") 		{ $hash_imports{"fr.ens.transcriptome.nividic.om.filters.BioAssayFilter"} = 1; }
		elsif ($type eq "Annotation") 				{ $hash_imports{"fr.ens.transcriptome.nividic.om.Annotation"} = 1; }
		elsif ($type eq "BiologicalObject") 	{ $hash_imports{"fr.ens.transcriptome.nividic.om.BiologicalObject"} = 1; }
		elsif ($type eq "BiologicalObjectFilter") { $hash_imports{"fr.ens.transcriptome.nividic.om.filters.BiologicalObjectFilter"} = 1; }
		elsif ($type eq "ExpressionMatrix") 	{ $hash_imports{"fr.ens.transcriptome.nividic.om.ExpressionMatrix"} = 1; }
		elsif ($type eq "ExpressionMatrixDimension" || $type eq "ExpressionMatrixDimension[]" ) { $hash_imports{"fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension"} = 1; }
		elsif ($type eq "ExpressionMatrixFilter") { $hash_imports{"fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter"} = 1; }
		elsif ($type eq "BiologicalList") 		{ $hash_imports{"fr.ens.transcriptome.nividic.om.BiologicalList"} = 1; }
		elsif ($type eq "Design") 						{ $hash_imports{"fr.ens.transcriptome.nividic.om.design.Design"} = 1; }
		elsif ($type eq "Translator") 				{ $hash_imports{"fr.ens.transcriptome.nividic.om.translators.Translator"} = 1; }
		elsif ($type eq "BioAssayUtils") 			{ $hash_imports{"fr.ens.transcriptome.nividic.om.BioAssayUtils"} = 1; }
		elsif ($type eq "ExpressionMatrixUtils") 	{ $hash_imports{"fr.ens.transcriptome.nividic.om.ExpressionMatrixUtils"} = 1; }
		elsif ($type eq "BiologicalListFilter") { $hash_imports{"fr.ens.transcriptome.nividic.om.filters.BiologicalListFilter"} = 1; }
		elsif ($type eq "List<String>") 	{ $hash_imports{"java.util.List"} = 1; }
		else { print "Import: Unknow import: $type\n"; exit 0; }

	}
	
	my $imports = "";

	foreach my $class (keys %hash_imports) {
		$imports .= "import $class;\n";
	}
	
	#
	# Getter code
	#
	
	my $getters_code;
	my $n = scalar @array_processor_input_names;
	
	for (my $i=0; $i<$n; $i++) {
		
		my $name = $array_processor_input_names[$i];
		my $type = $array_processor_input_types[$i];
		my $primitive_type = "";
		#if ($class_name eq 'addBiologicalListToBiologicalList') { print "$name\n"; }
		$getters_code .= &generate_getter_code($name, $type);	
	}



	#if ( !($object_type eq '')) { $getters_code.=&generate_getter_code(to_lower_case($object_type), $object_type); }
	
	
	#
	# Test parameter
	#
	
	my $test_parameters;
	for (my $i=0; $i<$n; $i++) {
	
		my $name = $array_processor_input_names[$i];
		my $type = $array_processor_input_types[$i];
	
			if ($type eq "int" || $type eq "float" || $type eq "double" || $type eq "boolean") { next; }
	
		$test_parameters .= "\t\t\tif ($name == null) throw new TaskExecutionException(\"The $name parameter is null.\");\n";
	}
	
	#
	# Main code
	#
	
	my $var_to_return = &first_to_lower_case($object_type);
	my $main_code = "\t\t\t";


	if ($utility_class eq '') {
	
		if ( !($return_type eq "void")) {
	
			$main_code .= "final $return_type result = ";	
		}
	
		$main_code .= "$var_to_return.$funct(";
	} else {


		if ($return_type eq "void") { $main_code .= "$utility_class.$funct("; }
		else { $main_code .= "final $return_type result = $utility_class.$funct(";	 }



	}

	$n = scalar @array_method_arguments;
	for (my $i=0; $i<$n; $i++) {
	
		if ($i>0) { $main_code .=" ,"; }
		my $arg = &trim($array_method_arguments[$i]);
		my ($type, $name) = split /\s+/, $arg;
		$main_code .=$name;
	
	}
	
	$main_code .= ");\n";
	
	
	
	
	#
	# Setter code
	#
	
	my $setters_code = "";
	
	
	if ( ($return_type eq "void")) {
	
	
		$setters_code .="\t\t\tfinal $object_type result = $var_to_return;\n";
	
	}
	



	$setters_code .="\t\t\t";

	#my $method_return_type = $object_type;
	my $method_return_type;

	if ($return_type eq "void") { $method_return_type = $object_type; }
	else { $method_return_type = $return_type; }

	if ($method_return_type ne "byte" 
			&& $method_return_type ne "short" 
			&& $method_return_type ne "int" 
			&& $method_return_type ne "long" 
			&& $method_return_type ne "float" 
			&& $method_return_type ne "double" 
			&& $method_return_type ne "boolean" 
			&& $method_return_type ne "char" )  { $setters_code .="if (result != null) "; }

	$setters_code .="outputs.put";

	if ($method_return_type eq "String") { $setters_code .="String"; }
	elsif ($method_return_type eq "boolean") { $setters_code .="Boolean"; }
	elsif ($method_return_type eq "int") { $setters_code .="Int"; }
	elsif ($method_return_type eq "double") { $setters_code .="Double"; }
	elsif ($method_return_type eq "BiologicalObject") { $setters_code .="BiologicalObject"; }
	elsif ($method_return_type eq "BioAssay") { $setters_code .="BioAssay"; }
	elsif ($method_return_type eq "ExpressionMatrix") { $setters_code .="ExpressionMatrix"; }
	elsif ($method_return_type eq "ExpressionMatrixDimension") { $setters_code .="ExpressionMatrixDimension"; }
	elsif ($method_return_type eq "ExpressionMatrixDimension[]") { $setters_code .="ExpressionMatrixDimensionArray"; }
	elsif ($method_return_type eq "Design") { $setters_code .="Design"; }
	elsif ($method_return_type eq "BiologicalList") { $setters_code .="BiologicalList"; }
	elsif ($method_return_type eq "Annotation") { $setters_code .="Annotation"; }
	elsif ($method_return_type eq "Translator") { $setters_code .="Translator"; }
	elsif ($method_return_type eq "String[]") { $setters_code .="StringArray"; }
	elsif ($method_return_type eq "int[]") { $setters_code .="IntArray"; }
	elsif ($method_return_type eq "double[]") { $setters_code .="DoubleArray"; }
	elsif ($method_return_type eq "List<String>") { $setters_code .="StringList"; }
	else { print "setter: Unknown: $method_return_type\n"; exit 0; }
	



	$setters_code .="(\"";
	
	if ($return_type eq "void") { $setters_code .= &first_to_lower_case($object_type); }
	else { $setters_code .= "output"; }	
	
	$setters_code .="\", result);\n";
	
	
	
	my $code = "/*
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
 * These should be listed in \@author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package $package;

import java.util.Map;

import org.embl.ebi.escience.baclava.DataThing;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;
import fr.ens.transcriptome.nividic.taverna.NividicLocalWorker;
import fr.ens.transcriptome.nividic.taverna.NividicDataThingAdapter;

$imports

/**
* $description
* \@author Laurent Jourdren
*/
public class $class_name implements NividicLocalWorker {

	/**
	 * Get the input names of the processor.
	 * \@return an array of Strings
	 */
	public String[] inputNames() {
		return new String[] {$input_names};
	}

	/**
	 * Get the input types of the processor.
	 * \@return an array of Strings
	 */
	public String[] inputTypes() {
		return new String[] {$input_types};
	}

	/**
	 * Get the output names of the processor.
	 * \@return an array of Strings
	 */
	public String[] outputNames() {
		return new String[] {$output_names};
	}

	/**
	 * Get the output types of the processor.
	 * \@return an array of Strings
	 */
	public String[] outputTypes() {
		return new String[] {$output_types};
	}

	/**
	 * Main method of the processor.
	 * \@throws TaskExecutionException if error occurs while executing the processor
	 */
	public Map<String, DataThing> execute(Map<String, DataThing> inputs)
		throws TaskExecutionException {

		try {

			//
			// Get the parameters of the processor
			//

			NividicDataThingAdapter ndta = new NividicDataThingAdapter(inputs);

$getters_code

			//
			// Test the values of the processor
			//

$test_parameters

			//
			// Run it
			//

$main_code

			//
			// Set the return values
			//

			NividicDataThingAdapter outputs = new NividicDataThingAdapter();

$setters_code

			return outputs.getMap();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new TaskExecutionException(ex);
		}
	}
}\n";
	
	return $code;

}


sub write_code {

	my ($class_name, $object_type, $utility_class, $method, $description, $package) = @_;

	open OUT, ">$class_name.java";
	print OUT &createClassCode($class_name, $object_type, $utility_class, $method, $description, $package);
	close OUT;

	open OUT, ">>nividic.properties";
	print OUT "$package.$class_name = $object_type:$class_name\n";
	close OUT;

	open OUT, ">>fr.ens.transcriptome.nividic.taverna.NividicLocalWorker";
	print OUT "$package.$class_name\n";
	close OUT;
}

my $result = &createClassCode(	"SetBiologicalObjectName",
																"BioAssay", 
																"BioAssayUtils",
																"void setName(String name);",
																"This wrapper set the name of a BioAssay", 
																$package); 


open IN, "</home/jourdren/workspace/nividic-taverna/api-generator.txt";

while (<IN>) {
	chomp;
	next if (/^#/);
	my $line = $_;

	if (!($line eq "")) {
		#print "$line\n";
		my ($class_name, $object_type, $utility_class, $method, $description) = split /\t/, $line;
		$class_name = &first_to_upper_case($class_name);
		if ($description eq "") { $description = "Generated wrapper class."; }
		&write_code($class_name, $object_type, $utility_class, $method, $description, $package);
	}

}




