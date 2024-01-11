package com.itshixun.acptoolkit

import ai.grazie.nlp.langs.Language
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.ProcessingContext
import com.intellij.lang.LanguageParserDefinitions
class AcpStringLiteralPattern: PatternCondition<PsiElement>("AcpStringLiteralPattern") {
    override fun accepts(psi: PsiElement,context: ProcessingContext?): Boolean {
        val language  = PsiUtilCore.findLanguageFromElement(psi)
        println("AcpStringLiteralPattern accepts language:$language")
        val definition = LanguageParserDefinitions.INSTANCE.forLanguage(language) ?: return false
        val tokens = TokenSet.orSet(
                definition.commentTokens,
                definition.stringLiteralElements
        )

        val node = psi.node ?: return false
        println(node.elementType)
        if(tokens.contains(node.elementType)) {
            return true
        }
        return false
    }
}