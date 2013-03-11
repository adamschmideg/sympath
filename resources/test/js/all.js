test( "hello test", function() {
  ok( 1 == "1", "Passed!" );
});

var j2c = sympath.private$.from_host_form;

var js_form = {
  customer: {
    name: "John"
  },
  cart: [
    {item: "Book", amount: 2},
    {item: "Bread", amount: 1},
  ]
};
var clj_form = j2c(js_form);

// private stuff
test("js->clj", function() {
  var key = cljs.core.first(cljs.core.keys(clj_form));
  ok(cljs.core.keyword_QMARK_(key));
});

test("submap?", function() {
  var container = j2c({a: 3, b: 4});
  ok(sympath.core.submap_QMARK_(container, j2c({})), "empty");
  ok(sympath.core.submap_QMARK_(container, j2c({a: 3})), "submap");
  ok(! sympath.core.submap_QMARK_(container, j2c({a: 4})), "different value");
  ok(! sympath.core.submap_QMARK_(container, j2c({c: 4})), "different key");
});

test("match_selector", function() {
  deepEqual(true,
    sympath.core.match_selector(clj_form, "/cart/0/amount", "/cart/*/amount"));
});

// public stuff
test("parse_path", function() {
  deepEqual(sympath.public$.parse_path("a/3/b"), ["a", 3, "b"]);
});

test("demo", function() {
  var field;
  field = sympath.demo.field(js_form, "/cart/0/amount");
  deepEqual(field, {value: 2});
  js_form.cart[0].amount = 13;
  field = sympath.demo.field(js_form, "/cart/0/amount");
  deepEqual(field, {
    value: 13,
    error: {
      error: "unlucky",
      args: {
        value: 13
      }
    }
  });
});

