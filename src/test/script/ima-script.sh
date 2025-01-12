#!/bin/bash

# Directory containing the .ass files
input_dir="src/test/results/deca/codegen"

# Loop through each .ass file in the directory
for file in "$input_dir"/*.ass; 
do
    echo "$file"
    ima "$file"
done