package com.unravel.scout.model.enums;

enum LabeledEnumOperations {
  DEFAULT("default_value");

  public String label;

  LabeledEnumOperations(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public static LabeledEnumOperations getValueForLabel(String label) {
    for (LabeledEnumOperations t : values()) {
      if (t.label.equals(label)) {
        return t;
      }
    }
    return null;
  }
}
