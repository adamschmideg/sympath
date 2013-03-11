
# Errors when running under node.js

When running

  module = "./resources/public/js/main-debug"; // "./resources/private/js/unit-test"
  m = require(module);

It throws one of these errors

  * TypeError: Cannot set property 'XXX' of undefined
  * ReferenceError: 

The reason is that `goog.provide` doesn't work properly under node.js,
but the clojure namespaces got properly exported to the global window
when running in a browser.

# ClojureScript and JavaScript interop
When returning a value from clojurescript, make it usable for
javascript.  Avoid sequences, keywords, symbols; use `(clj->js form)`
first.  You will probably need its inverse, `js->clj` when getting
arguments from javascript.

# API used by gui
The `menu` function should return a list of objects:

    menuitem = {
      group: 'cart'
      name: 'cart_checkout'
      label: 'Checkout {{id}}'  // yes, it may be a template
      // status flags
      available: true
      mandatory: false
      visited: true
    }
