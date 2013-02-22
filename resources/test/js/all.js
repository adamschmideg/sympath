test( "hello test", function() {
  ok( 1 == "1", "Passed!" );
});

test("parse_path", function() {
  deepEqual(tree.core.parse_path("a/3/b"), ["a", 3, "b"]);
});