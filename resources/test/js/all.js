test( "hello test", function() {
  ok( 1 == "1", "Passed!" );
});

var js_form = {
  customer: {
    name: "John"
  },
  cart: [
    {item: "Book", amount: 2},
    {item: "Bread", amount: 1},
  ]
};
var clj_form = sympath.private$.from_host_form(js_form);

test("parse_path", function() {
  deepEqual(sympath.public$.parse_path("a/3/b"), ["a", 3, "b"]);
});

test("match_selector", function() {
  deepEqual(true,
    sympath.core.match_selector(clj_form, "/cart/0/amount", "/cart/*/amount"));
});

test("demo", function() {
  var field;
  field = sympath.demo.field(js_form, "/cart/0/amount");
  deepEqual(field, {value: 2});
  js_form.cart[0].amount = 13;
  field = sympath.demo.field(js_form, "/cart/0/amount");
  deepEqual(field, {value: 13, error: {error: "unlucky"}});
});

