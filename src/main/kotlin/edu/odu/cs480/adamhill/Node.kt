package edu.odu.cs480.adamhill

class Node<T>(val value: T) {
    val childNodes: MutableList<Node<T>> = mutableListOf() // initially empty list

    fun addChild(child: Node<T>) {
        childNodes.add(child)
    }

    fun addChildren(vararg children: Node<T>) {
        children.forEach { addChild(it) }
    }

    override fun toString(): String = value.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var hash = 349
        if (value != null) hash = 337 * hash + value.hashCode()
        //hash = 337 * hash + childNodes.hashCode()
        return hash
    }
}