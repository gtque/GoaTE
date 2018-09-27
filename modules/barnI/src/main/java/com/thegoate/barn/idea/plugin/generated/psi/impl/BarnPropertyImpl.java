// This is a generated file. Not intended for manual editing.
package com.thegoate.barn.idea.plugin.generated.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.thegoate.barn.idea.plugin.generated.psi.*;

public class BarnPropertyImpl extends ASTWrapperPsiElement implements BarnProperty {

  public BarnPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BarnVisitor visitor) {
    visitor.visitProperty(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BarnVisitor) accept((BarnVisitor)visitor);
    else super.accept(visitor);
  }

}
