# cig

A Clojure library designed to ... well, that part is up to you.

## Usage

FIXME

## Development

The official and painful version:

1. Start `lein cljsbuild auto` in a terminal
2. Start `lein ring server-headless 3000 &`
3. Start an in-browser REPL

Refreshing the browser will reflect the changes you made to a source
file.  If you required a namespace, it will be changed to the new
version.

The unofficial version is to put most things into `clj` files and
develop a normal Clojure project.  Then make these files cljsbuild
crossovers, and compile them to javascript.

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
