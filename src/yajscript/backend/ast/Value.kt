package yajscript.backend.ast

import yajscript.backend.type.Type

abstract class Value : Node() {
    abstract val value : Type
}