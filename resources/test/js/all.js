test( "hello test", function() {
  ok( 1 == "1", "Passed!" );
});

test("parse_path", function() {
  deepEqual(sympath.public$.parse_path("a/3/b"), ["a", 3, "b"]);
});

test("demo", function() {
  var form = {
    customer: {
      name: "John"
    },
    cart: [
      {item: "Book", amount: 2},
      {item: "Bread", amount: 1},
    ]
  };
  var field;
  field = sympath.demo.field(form, "/cart/0/amount");
  deepEqual(field, {value: 2});
  form.cart[0].amount = 13;
  field = sympath.demo.field(form, "/cart/0/amount");
  deepEqual(field, {value: 13, error: {error: "unlucky"}});
});

