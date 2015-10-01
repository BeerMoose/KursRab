import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class InputForm extends JFrame {
    int width = 630, height = 450;
    double eps, beta;
    int n;
    double a, b;
    int flagMetod;          //Номер метода, который будем использовать(0-3)
    JTextArea textAreaN, textAreaEps, textAreaBeta, textAreaA, textAreaB;
    JLabel labelAnswer, labelIterTitle, labelNevTitle, labelIterRes, labelNevRes, labelLogoMetod;
    JButton buttonStart, buttonReCountX0;
    JComboBox comboBox;
    Matrix x0;
    String items[] = {
            "Метод Пузынина",
            "Метод Пузынина(Мод)",
            "Метод 2",
            "Метод 3",
            "Метод 3(Мод)",
            "Метод 4",
            "Метод 5",
            "Метод 6"
    };
    public InputForm(){
        super("Курсовая работа");
        //-------Создание формы
        setLayout(null);
        setSize(width, height);
        //--------------------

        createBaseVarOnForm();

        //-------Создание формы
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        //---------------------
    }
    //Создание полей и кнопок на форме
    void createBaseVarOnForm(){
        //Эта штука нужна для фона, если он вдруг понадобится
        //JPanel fon = new JPanel(); fon.setBounds(0, 0, width, height); fon.setBackground(Color.PINK); add(fon);

        //Шрифты
        Font myFontLabel = new Font("tahoma", 1,20);
        Font myFontText = new Font("tahoma", 1,15);
        Font myFontAnswer = new Font("tahoma", 1, 12);

        //Объявление и расположение полей, кнопок и меток
        JLabel labelN = new JLabel("n=");                               labelN.setBounds(40, 30, 30, 20);           labelN.setFont(myFontLabel);    add(labelN);
        JLabel labelEps = new JLabel("<html>&#949=");                 labelEps.setBounds(40, 60, 30, 20);         labelEps.setFont(myFontLabel);    add(labelEps);
        JLabel labelBeta = new JLabel("<html>&#946<sub>0</sub>=");   labelBeta.setBounds(40, 90, 70, 30);        labelBeta.setFont(myFontLabel);    add(labelBeta);
        JLabel labelRange = new JLabel("Диапазон:");                labelRange.setBounds(150, 30, 160, 20);     labelRange.setFont(myFontLabel);    add(labelRange);
        JLabel labelA = new JLabel("a");                                labelA.setBounds(280, 30, 15, 20);          labelA.setFont(myFontLabel);    add(labelA);
        JLabel labelB = new JLabel("- b");                              labelB.setBounds(330, 30, 45, 20);          labelB.setFont(myFontLabel);    add(labelB);
        JLabel labelSol = new JLabel("Решение:");                     labelSol.setBounds(70, 150, 150, 20);       labelSol.setFont(myFontLabel);    add(labelSol);

        labelLogoMetod = new JLabel();                          labelLogoMetod.setBounds(280, 240, 350, 150);                                       add(labelLogoMetod);
        labelAnswer = new JLabel();                                labelAnswer.setBounds(60, 180, 200, 320);   labelAnswer.setFont(myFontAnswer);   add(labelAnswer);
        labelIterTitle = new JLabel("Итераций:");               labelIterTitle.setBounds(280, 150, 300, 20);labelIterTitle.setFont(myFontLabel);    add(labelIterTitle);
        labelIterRes = new JLabel();                              labelIterRes.setBounds(280, 170, 300, 20);  labelIterRes.setFont(myFontLabel);    add(labelIterRes);
        labelNevTitle = new JLabel("Невязка:");                  labelNevTitle.setBounds(280, 200, 300, 20); labelNevTitle.setFont(myFontLabel);    add(labelNevTitle);
        labelNevRes = new JLabel();                                labelNevRes.setBounds(280, 220, 300, 20);   labelNevRes.setFont(myFontLabel);    add(labelNevRes);

        textAreaN = new JTextArea();                                 textAreaN.setBounds(90, 32, 50, 20);        textAreaN.setFont(myFontText);     add(textAreaN);
        textAreaEps = new JTextArea();                             textAreaEps.setBounds(90, 62, 50, 20);      textAreaEps.setFont(myFontText);     add(textAreaEps);
        textAreaBeta = new JTextArea();                           textAreaBeta.setBounds(90, 92, 50, 20);     textAreaBeta.setFont(myFontText);     add(textAreaBeta);
        textAreaA = new JTextArea();                                 textAreaA.setBounds(295, 35, 30, 20);       textAreaA.setFont(myFontText);     add(textAreaA);
        textAreaB = new JTextArea();                                 textAreaB.setBounds(360, 35, 30, 20);       textAreaB.setFont(myFontText);     add(textAreaB);

        comboBox = new JComboBox(items);                              comboBox.setBounds(150, 70, 250, 30);       comboBox.setFont(myFontText);     add(comboBox);
        buttonStart = new JButton("Посчитать");                    buttonStart.setBounds(420, 20, 160, 40);    buttonStart.setFont(myFontText);     add(buttonStart);
        buttonReCountX0 = new JButton("Пересчитать х0");       buttonReCountX0.setBounds(420, 70, 160, 40);buttonReCountX0.setFont(myFontText);     add(buttonReCountX0);

        //Задание кнопкам их функций
        //Кнопка начала работы(Посчитать)
        ActionListener buttonStartListener = new ButtonStartListener();             buttonStart.addActionListener(buttonStartListener);
        //Кнопка пересчёта х0
        ActionListener buttonReCountX0Listener = new ButtonReCountX0Listener(); buttonReCountX0.addActionListener(buttonReCountX0Listener);

        //Установка расположения текста на метках
        labelAnswer.setVerticalAlignment(JLabel.TOP);
        labelIterTitle.setHorizontalAlignment(JLabel.LEFT);
        labelIterRes.setHorizontalAlignment(JLabel.LEFT);
        labelNevTitle.setHorizontalAlignment(JLabel.LEFT);
        labelNevRes.setHorizontalAlignment(JLabel.LEFT);
    }
    //Считывание этих полей
    public void readBaseVar(){
        n       = Integer.parseInt(textAreaN.getText());
        eps     = Double.parseDouble(textAreaEps.getText());
        beta    = Double.parseDouble(textAreaBeta.getText());
        a       = Double.parseDouble(textAreaA.getText());
        b       = Double.parseDouble(textAreaB.getText());

//        n = 3; eps = 1e-6; beta = 0.001; a = -4; b = 4; //Для тестов, когда лень вводить

        if(x0 == null || x0.n != n) { //Если до этого не считали х0 или изменили размерность, то пересчитываем х0
            x0 = generateX0();
            printX0toLogFile();
        }
        flagMetod = comboBox.getSelectedIndex();//Получаем номер выбранного метода
        labelLogoMetod.setIcon(new ImageIcon("img/" + (flagMetod+1) + ".png"));//Выводим картинку для метода
        processResult(new Solution(n, eps, beta, a, b, x0, flagMetod).printAll());//Обрабатываем результаты, которые получили при решении
    }
    //Класс для добавлении функций к кнопке
    public class ButtonStartListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            readBaseVar();
        }
    }
    public class ButtonReCountX0Listener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            x0 = generateX0();
            printX0toLogFile();
        }
    }
    //Генерируем х0
    Matrix generateX0(){
        Matrix result = new Matrix(n, 1);
        for (int i = 0; i < n; i++) {
            result.value[i][0] = a + Math.random()*(b - a);
        }
        return result;
    }
    //Обработка результатов. Вывод на метку значений
    void processResult(String[] result){
        labelIterRes.setText(result[0]);
        labelNevRes.setText(result[1]);
        labelAnswer.setText(result[2]);
    }
    //Вывод х0 в файл. Для хранения.(Надо исправить дозапись)
    void printX0toLogFile(){
        FileWriter fw = null;
        try{
            fw = new FileWriter("log.txt", true);
            for (int i = 0; i < x0.n; i++) {
                fw.write(x0.value[i][0] + "\n");
            }
            fw.append("\n");
            fw.close();
        }
        catch(IOException e){

        }

    }
}
