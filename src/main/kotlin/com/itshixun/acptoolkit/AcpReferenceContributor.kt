package com.itshixun.acptoolkit

import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.*

class AcpReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
                PsiJavaPatterns.literalExpression()
                , AcpReferenceProvider())
    }
}