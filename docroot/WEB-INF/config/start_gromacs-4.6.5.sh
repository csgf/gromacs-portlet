#!/bin/bash

chmod +x ./curl
env > env.log
ls -al ${PWD}

echo "-----------------------------------------------------------------------------------"
echo "Running host ..." `hostname -f`
echo "IP address ....." `/sbin/ifconfig | grep "inet addr:" | head -1 | awk '{print $2}' | awk -F':' '{print $2}'`
echo "Kernel ........." `uname -r`
echo "Distribution ..." `head -n1 /etc/issue`
echo "Arch ..........." `uname -a | awk '{print $12}'`
echo "CPU  ..........." `cat /proc/cpuinfo | grep -i "model name" | head -1 | awk -F ':' '{print $2}'`
echo "Memory ........." `cat /proc/meminfo | grep MemTotal | awk {'print $2'}` KB
echo "Partitions ....." `cat /proc/partitions`
echo "Uptime host ...." `uptime | sed 's/.*up ([^,]*), .*/1/'`
echo "Timestamp ......" `date`
echo "-----------------------------------------------------------------------------------"

export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:.
export VO_NAME=$(voms-proxy-info -vo)
export VO_VARNAME=$(echo ${VO_NAME} | sed s/"\."/"_"/g | sed s/"-"/"_"/g | awk '{ print toupper($1) }')
export VO_SWPATH_NAME="VO_"$VO_VARNAME"_SW_DIR"
export VO_SWPATH_CONTENT=$(echo $VO_SWPATH_NAME | awk '{ cmd=sprintf("echo $%s",$1); system(cmd); }')
#export OMP_NUM_THREADS=1
export path="/dpm"

INPUT_FILE=`basename $1`
MIDDLEWARE=$2
METADATA_HOST=$3
USERNAME=$4
DESC="$5"
PROXY_RFC=${X509_USER_PROXY}
export DPM_HOST=$6
export DPNS_HOST=$6
chmod 600 ${PROXY_RFC}

DATETIME=`date +%Y%m%d%H%M%S`
RELEASE=`uname -r | awk -F'.x86_64' '{print $1}' | awk -F'.' '{print $NF}'`

# Function to fetch the DPM paths with dpns-* CLI
fetch_DPM_folder_recurse()     
{
    for i in $(dpns-ls /$1)
    do
        if [ $? -eq 0 ] ; then
            if [ "X${i}" != "Xhome" ] ; then
                    fetch_DPM_folder_recurse "$1/$i"
            else 
                echo "$1/$i"
            fi
        fi
    done
}

if [ "X${MIDDLEWARE}" = "XgLite" ] ; then
	echo;echo "Selected Middleware/Infrastructure is gLite!"
	WELCOME=" [ STARTING GROMACS-4.6.5 ] in e-Infrastructures based on the EMI-gLite Middleware"
	
	if [ "X${RELEASE}" == "Xel5" ] ; then
		GROMACS_BIN_PATH=${VO_SWPATH_CONTENT}/gromacs-4.6.5-sl5-x86_64-gccany/bin
	else
        	GROMACS_BIN_PATH=${VO_SWPATH_CONTENT}/gromacs-4.6.5-sl6-x86_64-gccany/bin
	fi

	echo;echo "[ GROMACS Settings ]"
	echo "-----------------------------------------"
	echo "MIDDLEWARE        : "${MIDDLEWARE}
	echo "VO_NAME           : "${VO_NAME}
	echo "VO_VARNAME        : "${VO_VARNAME}
	echo "DEFAULT_SE        : "${DPM_HOST}
	echo "VO_SWPATH_NAME    : "${VO_SWPATH_NAME}
	echo "VO_SWPATH_CONTENT : "${VO_SWPATH_CONTENT}
	echo "GROMACS_BIN_PATH  : "${GROMACS_BIN_PATH}
	echo "-----------------------------------------"
	echo "INPUT_FILE          : "${INPUT_FILE}
	echo "METADATA_HOST       : "${METADATA_HOST}
	echo "USERNAME            : "${USERNAME}
	echo "DESCRIPTION         : "${DESC}
	echo "RFC PROXY           : "${PROXY_RFC}
	echo "-----------------------------------------"
	
	# Check if the STORAGE PATH does already exist on the EMI-3 DPM Storage Element
	echo;echo "[ CHECHING/CREATING the Storage Path ... ]"
	DPM_STORAGE_PATH=`fetch_DPM_folder_recurse $path`
	dpns-mkdir ${DPM_STORAGE_PATH}/${VO_NAME}/GROMACS 2>/dev/null >/dev/null

	if [ $? -eq 1 ] ; then
	echo "The Storage Path already does exist on the SE"
	else
	echo "The Storage Path has been successfully created on the SE"
	echo dpns-mkdir ${DPM_STORAGE_PATH}/${VO_NAME}/GROMACS 2>/dev/null >/dev/null
	fi
fi

if [ "X${MIDDLEWARE}" = "XGARUDA" ] ; then
	echo;echo "Selected Middleware/Infrastructure is GARUDA based on Globus Toolkit (v4.0.8)"
	WELCOME=" [ STARTING GROMACS-4.5.5 ] in GARUDA! "

       	GROMACS_BIN_PATH=/usr/local/GARUDA/apps/Gromacs-4.5.5

	echo;echo "[ GROMACS Settings ]"
	echo "-----------------------------------------"
	echo "MIDDLEWARE        : "${MIDDLEWARE}
	echo "METADATA_HOST       : "${METADATA_HOST}
	echo "DEFAULT_SE        : "${DPM_HOST}
	echo "GROMACS_BIN_PATH  : "${GROMACS_BIN_PATH}
	echo "-----------------------------------------"
	echo "INPUT_FILE          : "${INPUT_FILE}
	echo "USERNAME    	    : "${USERNAME}
	echo "DESCRIPTION         : "${DESC}
	echo "RFC PROXY     	    : "${PROXY_RFC}
	echo "-----------------------------------------"
fi

echo;echo ${WELCOME}
echo
echo "  ██████╗ ██████╗  ██████╗ ███╗   ███╗ █████╗  ██████╗███████╗ "
echo " ██╔════╝ ██╔══██╗██╔═══██╗████╗ ████║██╔══██╗██╔════╝██╔════╝ "
echo " ██║  ███╗██████╔╝██║   ██║██╔████╔██║███████║██║     ███████╗ "
echo " ██║   ██║██╔══██╗██║   ██║██║╚██╔╝██║██╔══██║██║     ╚════██║ "
echo " ╚██████╔╝██║  ██║╚██████╔╝██║ ╚═╝ ██║██║  ██║╚██████╗███████║ "
echo "  ╚═════╝ ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝ "

if [ -f ${GROMACS_BIN_PATH}/mdrun ] ; then
	echo;echo "[ STARTING GROMACS-4.6.5 with input file = "$INPUT_FILE " ]"
	#echo ${GROMACS_BIN_PATH}/mdrun -v -pd -s ${INPUT_FILE} -deffnm prueba
	#${GROMACS_BIN_PATH}/mdrun -v -pd -s ${INPUT_FILE} -deffnm prueba 2>gromacs.log >gromacs.log
	echo ${GROMACS_BIN_PATH}/mdrun -v -s ${INPUT_FILE} -deffnm prueba
        ${GROMACS_BIN_PATH}/mdrun -v -s ${INPUT_FILE} -deffnm prueba 2>gromacs.log >gromacs.log

	echo;echo "[ CHECHING for results ... ]"
	ls -al $PWD/

	echo;echo "[ CHECHING the CURL client libraries ... ]"
	ldd ./curl > curl.log 2> curl.log

        echo;echo "[ CREATING GROMACS output log files ]"
	if [ -f prueba.trr ] ; then
		tar cfz results_${USERNAME}_${DATETIME}.tar.gz \
			*.cpt *.gro *.edr *.log *.trr 2>/dev/null
	else
		tar cfz results_${USERNAME}_${DATETIME}.tar.gz \
		*.cpt *.gro *.edr *.log 2>/dev/null
	fi

	SIZE=`cat results_${USERNAME}_${DATETIME}.tar.gz | wc -c`

	#######################################
	# Using gLibrary Data Management APIs #
	#  to upload output files to Grid SE  #
	#######################################

	echo; echo "[ GETTING the short-lived URL where the actual file(s) should be uploaded ]"
	URL=`./curl -3 -k -E ${PROXY_RFC} https://${METADATA_HOST}/api/dm/put/${VO_NAME}/results_${USERNAME}_${DATETIME}.tar.gz/${DPM_HOST}${DPM_STORAGE_PATH}/${VO_NAME}/GROMACS/ \
	| grep -i redirect \
	| awk '{print $2}' \
	| awk -F',' '{print $1}' \
	| awk -F'"' '{print $2}' \
	| awk -F'"' '{print $1}'`

	if [ "X${URL}" != "X" ] ; then
		echo "URL = "${URL}
		echo; echo "[ UPLOADING output file(s) in progress ]"
		curl -T results_${USERNAME}_${DATETIME}.tar.gz -X PUT ${URL}

		echo; echo "[ ADDING a new entry with its metadata of a give type ]"
		TIMESTAMP=`date +%F' '%T`
		./curl -3 -k -E ${PROXY_RFC} -X POST \
	       -d "__Replicas=https://${DPM_HOST}${DPM_STORAGE_PATH}/${VO_NAME}/GROMACS/results_${USERNAME}_${DATETIME}.tar.gz&FileName=results_${USERNAME}_${DATETIME}.tar.gz&Size=${SIZE}&Description=${DESC}&FileType=GZIP&Creator=${USERNAME}&SubmissionDate=${TIMESTAMP}" \
	       https://${METADATA_HOST}/api/GROMACS/Jobs/
	else 
		echo "Some errors occurred during the file registration. Please, check log messages"
		echo "./curl -3 -k -E ${PROXY_RFC} https://${METADATA_HOST}/api/dm/put/${VO_NAME}/results_${USERNAME}_${DATETIME}.tar.gz/${DPM_HOST}${DPM_STORAGE_PATH}/${VO_NAME}/GROMACS/"
		./curl -3 -k -E ${PROXY_RFC} \
	        https://${METADATA_HOST}/api/dm/put/${VO_NAME}/results_${USERNAME}_${DATETIME}.tar.gz/${DPM_HOST}${DPM_STORAGE_PATH}/${VO_NAME}/GROMACS/
	fi
else
       echo;echo "[ STARTING GROMACS-4.6.5 with input file = "$INPUT_FILE " ]"
       echo "[ ABORT ] No such file or directory!"
fi

echo

cat <<EOF >> output.README
#
# README - GROMACS
#
# Giuseppe LA ROCCA, INFN, Italy
# <mailto:giuseppe.larocca@ct.infn.it>
#

GROMACS is a versatile package to perform molecular dynamics, i.e. simulate the Newtonian equations of motion for systems 
with hundreds to millions of particles.
 
It is primarily designed for biochemical molecules like proteins, lipids and nucleic acids that have a lot of complicated 
bonded interactions, but since GROMACS is extremely fast at calculating the non-bonded interactions (that usually dominate 
simulations) many groups are also using it for research on non-biological systems, e.g. polymers. 

In case of success GROMACS will produce the following list of files:

~ std.txt:	the standard output file;
~ std.err:  	the standard error file;
~ gromacs.log:	the gromacs log file.
~ some additional log files.

GROMACS output files 'results_${USERNAME}_${DATETIME}.tar.gz' will be available for downloading through the GROMACS Browse service.
EOF
