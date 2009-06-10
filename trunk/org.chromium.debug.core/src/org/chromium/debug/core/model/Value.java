// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.debug.core.model;

import org.chromium.sdk.JsArray;
import org.chromium.sdk.JsDataType;
import org.chromium.sdk.JsValue;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * A generic (non-array) implementation of IValue using a JsValue instance.
 */
public class Value extends DebugElementImpl implements IValue {

  private static final IVariable[] EMPTY_VARIABLES = new IVariable[0];

  private final JsValue value;

  private IVariable[] variables;

  public static Value create(DebugTargetImpl debugTarget, JsValue value) {
    if (JsDataType.TYPE_ARRAY == value.getReferenceType()) {
      return new ArrayValue(debugTarget, (JsArray) value);
    }
    return new Value(debugTarget, value);
  }

  Value(DebugTargetImpl debugTarget, JsValue value) {
    super(debugTarget);
    this.value = value;
  }

  @Override
  public String getReferenceTypeName() throws DebugException {
    return value.getReferenceType().toString();
  }

  @Override
  public String getValueString() throws DebugException {
    return value.getValueString();
  }

  @Override
  public IVariable[] getVariables() throws DebugException {
    if (variables == null) {
      if (value.asObject() != null) {
        variables = StackFrame.wrapVariables(getDebugTarget(), value.asObject().getProperties());
      } else {
        variables = EMPTY_VARIABLES;
      }
    }
    return variables;
  }

  @Override
  public boolean hasVariables() throws DebugException {
    return value.asObject() != null;
  }

  @Override
  public boolean isAllocated() throws DebugException {
    return false;
  }

  public JsValue getJsValue() {
    return value;
  }

}