package com.eju.baseadapter.section

open class SectionItem {
    class Item<T,S,K>(
        val item:T,
        val sort:S,
        val section:K
    ):SectionItem()

    class Section<K>(
        val section:K
    ):SectionItem()
}