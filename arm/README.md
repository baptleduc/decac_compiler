Pour compiler le mardown: 
```sh 

pandoc arm/doc_arm.md --listings -H arm/listings-setup.tex --toc -V geometry:"left=1cm, top=1cm, right=1cm, bottom=2cm" -V fontsize=12pt -f markdown+hard_line_breaks --standalone -t latex --pdf-engine xelatex --output arm/doc_ARM.pdf

```

