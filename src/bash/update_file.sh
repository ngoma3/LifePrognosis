#!/bin/bash

FILE=$1
OLD_LINE=$2
NEW_LINE=$3

sed -i "s|$OLD_LINE|$NEW_LINE|" $FILE
