package com.itshixun.acptoolkit

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.intellij.util.PsiIconUtil

class AcpCompletionProvider(): CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {


        // return early when there's not prefix
        var prefix = result.prefixMatcher.prefix
        println("prefix:${prefix}")
        if (prefix.isEmpty()) {
            return
        }
        val dictResult: CompletionResultSet

        val lastSpace = prefix.lastIndexOf(',')
        if (lastSpace >= 0 && lastSpace < prefix.length - 1) {
            prefix = prefix.substring(lastSpace + 1)
            dictResult = result.withPrefixMatcher(prefix)
        } else {
            dictResult = result
        }

        val length = prefix.length
        val firstChar = prefix[0]
        val isUppercase = Character.isUpperCase(firstChar)

        val acpDict = AcpUtils.getAcpDict()
        for (acp in acpDict) {
//            println("acp.name:${acp.name},prefix:${prefix}")
            if (acp.name.startsWith(prefix)) {
               val element =  LookupElementBuilder.create(acp.name).withIcon(PsiIconUtil.getProvidersIcon(acp.expression, 0))
                        .withTypeText(acp.name)
                dictResult.addElement(element)
            }
        }

    }



}
