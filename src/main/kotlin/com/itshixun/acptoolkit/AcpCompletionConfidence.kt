package com.itshixun.acptoolkit

import com.intellij.codeInsight.completion.CompletionConfidence
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import com.intellij.psi.util.elementType
import com.intellij.util.ThreeState

class AcpCompletionConfidence: CompletionConfidence() {
    override fun shouldSkipAutopopup(contextElement: PsiElement, psiFile: PsiFile, offset: Int): ThreeState {
//        return super.shouldSkipAutopopup(contextElement, psiFile, offset)
        println(contextElement.elementType)
        println("offset$offset")
        val expression =PsiTreeUtil.getParentOfType(contextElement, PsiLiteralExpression::class.java)
        if(AcpUtils.isAcpPsiLiteralExpression(expression as PsiElement)) {
            println("true")
            return ThreeState.NO
        }else {
            return super.shouldSkipAutopopup(contextElement, psiFile, offset)
        }
    }
}