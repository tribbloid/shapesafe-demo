CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

file=${CRDIR}/slide.md
out=${CRDIR}/target

mkdir $out
pandoc $file \
        -o $out/$file.pdf \
        -t beamer \
        --slide-level=2 \
        --pdf-engine=xelatex

pandoc $file \
        -o $out/$file.pdf \
        -t beamer \
        --slide-level=2 \
        --pdf-engine=xelatex \
        -V handout