CurrencyConverter

Проблемы приложения:
- Все данные выводятся с разделителем - запятой. Для осуществления конвертации разделителем должна быть точка. Либо вообще должно быть целое число. В общем, чтобы сконвертировалось - нужно в поле ввода стереть запятую 8(
- Числа в float. С валютой предпочтительнее работать в BigDecimal (если верить интернетам)

ToDo:
- попробовать библиотеку moshi
- поиграться со стилями
- попробовать прикрутить dagger2

В приложении использованы:
- Retrofit2
- ViewModel/LiveData ( https://developer.android.com/topic/libraries/architecture/viewmodel.html  https://habr.com/ru/post/332562/  )
- RecyclerView ( https://developer.android.com/reference/android/support/v7/widget/RecyclerView  )
- Иконки отсюда https://icon-icons.com/
