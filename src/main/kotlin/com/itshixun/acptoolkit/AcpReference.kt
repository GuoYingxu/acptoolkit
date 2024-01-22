// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.itshixun.acptoolkit

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.keymap.impl.KEYMAPS_DIR_PATH
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.IconUtil
import com.itshixun.acptoolkit.AcpDefinition

class AcpReference(element: PsiElement,range: TextRange ) : PsiReferenceBase<PsiElement?>(element,range), PsiPolyVariantReference {
    private val key = element.text
            .substring( range.startOffset,range.endOffset)
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement!!.project
        val properties: List<AcpDefinition> = AcpUtils.getAcpDict()
        val results: MutableList<ResolveResult> = ArrayList()
        for (property in properties) {
            results.add(PsiElementResolveResult(property.expression))
        }
        return results.toTypedArray<ResolveResult>()
    }

    override fun resolve(): PsiElement? {
        println("key:$key")
        val resolveResults = multiResolve(false)
        println(resolveResults.size)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val project = myElement!!.project
        val properties: List<AcpDefinition> = AcpUtils.getAcpDict()
        val variants: MutableList<LookupElement> = ArrayList()
        for (property in properties) {

                variants.add(LookupElementBuilder
                        .create(property.name)
//                        .withIcon(IconUtil.getProvidersIcon(property, 0)
                        .withTypeText(property.description)
                )
        }
        return variants.toTypedArray()
    }
}