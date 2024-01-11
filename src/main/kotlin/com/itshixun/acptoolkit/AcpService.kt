package com.itshixun.acptoolkit

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiShortNamesCache

@Service(Service.Level.PROJECT)
class AcpService(private val project:Project) {
    private var initing = false
    private var ready = false
    fun collectAcpDefinition() {
        if(initing) return
        initing = true
        println("size:${AcpCacheData.getInstance().acpDefinitionMap.size} and cleared")
        AcpCacheData.getInstance().clear()
        // 使用索引检索
       val psiDefinitionMethods =  PsiShortNamesCache.getInstance(project).getMethodsByName("doDefinePermissions", GlobalSearchScope.projectScope(project))
//               println(psiDefinitionMethods.size)
            psiDefinitionMethods.forEach {
                AcpUtils.collectAcpFromClass(it.containingClass!!)

            }
        initing =false
        ready = true
        println("acpDefinitionMap:::${AcpCacheData.getInstance().acpDefinitionMap.size}")
    }

    fun initialized():Boolean {
        return !initing && ready
    }
}