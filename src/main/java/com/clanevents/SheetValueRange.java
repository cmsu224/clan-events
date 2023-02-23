package com.clanevents;

import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class SheetValueRange {
    String sheet;
    ValueRange valueRange;
}
