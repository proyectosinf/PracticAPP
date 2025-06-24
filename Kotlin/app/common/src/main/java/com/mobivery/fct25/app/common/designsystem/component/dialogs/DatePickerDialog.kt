package com.mobivery.fct25.app.common.designsystem.component.dialogs

import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.mobivery.fct25.app.common.R

@Composable
fun DatePickerDialog(
    year: Int,
    month: Int,
    day: Int,
    maxDateMillis: Long? = null,
    minDateMillis: Long? = null,
    onDateSelected: (Int, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            R.style.MyDatePickerDialogTheme,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                onDateSelected(selectedYear, selectedMonth, selectedDay)
            },
            year,
            month,
            day
        ).apply {
            setOnDismissListener { onDismiss() }
            setOnCancelListener { onDismiss() }
            maxDateMillis?.let { datePicker.maxDate = it }
            minDateMillis?.let { datePicker.minDate = it }
        }

        datePickerDialog.show()
    }
}
