package com.itshixun.acptoolkit

class AcpCacheData {
    companion object {
        private var instance:AcpCacheData? = null
        fun getInstance():AcpCacheData {
            if(instance == null) {
                instance = AcpCacheData()
            }
            return instance!!
        }
    }
    var acpDefinitionMap:MutableMap<String,AcpDefinition> = mutableMapOf<String, AcpDefinition>()


    fun clear() {
        acpDefinitionMap.clear()
    }
}