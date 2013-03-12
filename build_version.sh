#!/bin/sh

#hg tip --template '{date|shortdate}
#sed -i -e "s/\$HASH\$/`hg id -q`/" resources/public/js/main-debug.js 
  #| sed -e "s/\$HASH\$/$HASH$gut/"
cat resources/public/js/main-debug.js \
  | sed -e "s/HASH/$HASH$gut/"
