#!/bin/bash
#
# Converts LDIF data to CSV.

# Show usage if we don't have the right params
if [ "$1" == "" ]; then
	echo ""
	echo "Usage: cat ldif.txt | $0 <attributes> [...]"
	echo "Where <attributes> contains a list of space-separated attributes to include in the CSV. LDIF data is read from stdin."
	echo ""
	exit 99
fi

ATTRS="$*"

c=0
while read line; do

	# Skip LDIF comments
	[ "${line:0:1}" == "#" ] && continue;

	# If this line is blank then it's the end of this record, and the beginning
	# of a new one.
	#
	if [ "$line" == "" ]; then

		output=""

		# Output the CSV record
		for i in $ATTRS; do

			eval data=\$RECORD_${c}_${i}
			output=${output}\"${data}\",

			unset RECORD_${c}_${i}

		done

		# Remove trailing ',' and echo the output
		output=${output%,}
		echo $output >> out.csv


		# Increase the counter
		c=$(($c+1))
	fi

	# Separate attribute name/value at the semicolon (LDIF format)
	attr=${line%%:*}
	value=${line#*: }

	# Save all the attributes in variables for now (ie. buffer), because the data
	# isn't necessarily in a set order.
	#
	for i in $ATTRS; do
		if [ "$attr" == "$i" ]; then
			eval RECORD_${c}_${attr}=\"$value\"
		fi
	done

done
