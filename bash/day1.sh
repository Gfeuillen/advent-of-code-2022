export INPUTFILE="resources/day1.txt"
#solution1
sed -e ':a' -e 'N' -e '$!ba' -e 's/\n\n/|/g; s/\n/ + /g ; s/|/\n/g ' $INPUTFILE | xargs -P 0 -I% sh -c 'expr %' | sort -rn | head -n 1
#solution2
sed -e ':a' -e 'N' -e '$!ba' -e 's/\n\n/|/g; s/\n/ + /g ; s/|/\n/g ' $INPUTFILE | xargs -P 0 -I% sh -c 'expr %' | sort -rn | head -n 3 | awk '{s+=$1}END{print s}'