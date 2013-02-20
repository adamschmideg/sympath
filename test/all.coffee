{assert} = require 'chai'
# Note: I can't require main-debug.js here.  Why?
m = require '../resources/public/js/main'

describe 'environment', ->
  it 'simple call', ->
    assert.equal 5, m.tree.core.add(2, 3)
