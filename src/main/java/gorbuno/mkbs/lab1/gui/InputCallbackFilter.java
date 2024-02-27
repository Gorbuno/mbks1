package gorbuno.mkbs.lab1.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class InputCallbackFilter extends DocumentFilter { // фильтр ввода с переделанным функционалом
    private static final String FILE_NAME_RESTRICTED_SYMBOLS = "[<>:\"/\\|?*]"; // символы запрещенные в имени файла
    boolean filenameFilterEnabled; // Если включено - то фильруется ввод запрещенных символов
    FilePublisher windowApp; // просто ссылка на исходное окно

    public InputCallbackFilter(boolean filenameFiltration, FilePublisher window) {
        windowApp = window;                               // устанавливаем режим фильтрации и сохраняем ссылку на исходное окно с кнопками
        filenameFilterEnabled = filenameFiltration;
    }

    @Override // переделанная процедура вставки/печати символа
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        string = filenameFilterEnabled ? excludeRestrictedSymbols(string) : string; //удаляем запрещенку если включена фильтрация
        super.insertString(fb, offset, string, attr);
        processTextChange();
    }

    @Override // переделанная процедура удаления
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
        processTextChange();
    }

    @Override // переделанная процедура замены
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        text = filenameFilterEnabled ? excludeRestrictedSymbols(text) : text; //удаляем запрещенку если включена фильтрация
        super.replace(fb, offset, length, text, attrs);
        processTextChange();
    }

    private void processTextChange() { // при ЛЮБОМ вводе
        windowApp.saveFileButton.setVisible(true); // активирует кнопку "Сохранить"
        windowApp.publishButton.setVisible(false); // и скрывает кнопку "Опубликовать"
    }

    private static boolean isFileNameSymbol(char c) { // проверяет является ли символ запрещенным
        return FILE_NAME_RESTRICTED_SYMBOLS.indexOf(c) < 0;
    }

    private static String excludeRestrictedSymbols(String str) { // удаляет из строки запрещенные символы
        StringBuilder sb = new StringBuilder(str);
        for (int i = sb.length() - 1; i >= 0; i--) {
            if (!isFileNameSymbol(sb.charAt(i))) {
                sb.deleteCharAt(i);
            }
        }
        return sb.toString();
    }
}

