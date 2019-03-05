// This is a generated file. Not intended for manual editing.
package com.thegoate.barn.idea.plugin.generated.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.thegoate.barn.idea.plugin.psi.BarnElementType;
import com.thegoate.barn.idea.plugin.psi.BarnTokenType;
import com.thegoate.barn.idea.plugin.generated.psi.impl.*;

public interface BarnTypes {

  IElementType PROPERTY = new BarnElementType("PROPERTY");

  IElementType COMMENT = new BarnTokenType("COMMENT");
  IElementType CRLF = new BarnTokenType("CRLF");
  IElementType KEY = new BarnTokenType("KEY");
  IElementType SEPARATOR = new BarnTokenType("SEPARATOR");
  IElementType VALUE = new BarnTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == PROPERTY) {
        return new BarnPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
