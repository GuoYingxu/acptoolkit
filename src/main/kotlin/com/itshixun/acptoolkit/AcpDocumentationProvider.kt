package com.itshixun.acptoolkit

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression

class AcpDocumentationProvider:DocumentationProvider   {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
//         super.generateDoc(element, originalElement)
        if(AcpUtils.isAcpPsiLiteralExpression(element!!)) {
            if(element.project.getService(AcpService::class.java).initialized()) {
                return AcpUtils.findRightsDescription((element as PsiLiteralExpression).value as String)
            }else {
                element.project.getService(AcpService::class.java).collectAcpDefinition();
                return AcpUtils.findRightsDescription((element as PsiLiteralExpression).value as String)
            }
        }
        return null
    }
}