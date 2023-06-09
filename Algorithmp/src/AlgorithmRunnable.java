import java.util.Random;
import java.util.concurrent.CompletableFuture;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.*;

import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

class sharedmemory {
	//각 정렬그래프 그려주는 스레드로 값을 전달하는것에 사용되는 클래스
    int[] array;
    private int redColumn = -1;
    private int greenColumn = -1;
    private int cyanColumn = -1;
    private int blueColumn = -1;
    private boolean runval = true;
    private int bubblelocx = 900;
    private int bubblelocy = 300;
    private int sizex = 640;

    private int allgo = 0;

    public synchronized void array(int[] array) {
        this.array = array;
    }

    public synchronized void getredc(int redColumn) {
        this.redColumn = redColumn;
    }

    public synchronized void getgreenc(int greenColumn) {
        this.greenColumn = greenColumn;
    }

    public synchronized void getcyanc(int cyanColumn) {
        this.cyanColumn = cyanColumn;
    }

    public synchronized void getbluec(int blueColumn) {
        this.blueColumn = blueColumn;
    }

    public synchronized void getrunval(boolean runval) {
        this.runval = runval;
    }

    public synchronized void getallgo(int allgo) {
        this.allgo = allgo;
    }

    public synchronized int putallgo() {
        return this.allgo;
    }

    public synchronized int putsizex() {
        return this.sizex;
    }

    public synchronized int putcyanc() {
        return this.cyanColumn;
    }

    public synchronized int putbluec() {
        return this.blueColumn;
    }

    public synchronized int putredc() {
        return this.redColumn;
    }

    public synchronized int putgreenc() {
        return this.greenColumn;
    }

    public synchronized int putblocx() {
        return this.bubblelocx;
    }

    public synchronized int putblocy() {
        return this.bubblelocy;
    }

    public synchronized int[] putarray() {
        return this.array;
    }

    public synchronized boolean putrunval() {
        return this.runval;
    }
}

class AlgorithmRunnable implements Runnable {
    private int algorithmChoice;
    private int allgo =0;
    private JTextPane textPane;
    int[] array;
    int[] bubblearray = new int[128];
    int[] heaparray = new int[128];
    int[] quickarray = new int[128];
    int[] insertarray = new int[128];
    int[] selectionarray = new int[128];

    // 생성자: 알고리즘 선택과 JTextPane를 받아서 초기화합니다.
    AlgorithmRunnable(int algorithmChoice, JTextPane textPane, int allgo) {
        this.algorithmChoice = algorithmChoice;
        this.textPane = textPane;
        this.allgo = allgo;
        sm.getallgo(allgo);
    }

    Thread tr1;
    Thread tr2;
    Thread tr3;
    Thread tr4;
    Thread tr5;
    sharedmemory sm = new sharedmemory();

    public int[] randomnum() {
        array = new int[128];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }

    @Override
    public void run() {
        bubblearray = randomnum();
        heaparray = randomnum();
        quickarray = randomnum();
        insertarray = randomnum();
        selectionarray = randomnum();


        tr1 = new Thread(new Bubble(sm));
        tr2 = new Thread(new Heap(sm));
        tr3 = new Thread(new Quick(sm));
        tr4 = new Thread(new Insert(sm));
        tr5 = new Thread(new Selection(sm));
        
        switch (algorithmChoice) {
            case 1:
                appendText("버블 정렬 실행...\n", null);
                sm.array(bubblearray);
                tr1.start();
                bubbleSort(bubblearray);

                break;
            case 2:
                appendText("힙 정렬 실행...\n", null);
                sm.array(heaparray);
                tr2.start();
                heapSort(heaparray);

                break;
            case 3:
                appendText("퀵 정렬 실행...\n", null);
                playSound("START.WAV");
                sm.array(quickarray);
                tr3.start();

                quickSort(quickarray, 0, quickarray.length - 1); // 퀵 정렬 실행

                break;
            case 4:
                appendText("삽입 정렬 실행...\n", null);
                sm.array(insertarray);
                tr4.start();
                insertionSort(insertarray);

                break;
            case 5:
                appendText("선택 정렬 실행...\n", null);
                sm.array(selectionarray);
                tr5.start();
                selectionSort(selectionarray);
                break;

            default:
                appendText("잘못된 선택.\n", null);
                break;
        }
        while (true) {
            try {
                Thread.sleep(5);

            } catch (InterruptedException e) {
                sm.getrunval(false);
                Thread.currentThread().interrupt(); // 인터럽트 발생 시 현재 스레드를 중단합니다.
            }
        }
    }

    // 사운드
    private void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 버블 정렬 알고리즘
    private void bubbleSort(int[] array) {
        boolean swapped;
        playSound("START.WAV"); // 버블 시작

        for (int i = 0; i < array.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < array.length - 1 - i; j++) {
                sm.getredc(j);

                if (array[j] > array[j + 1]) {
                    // array[j]와 array[j + 1]을 교환
                    sm.getredc(j + 1);

                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                    if (allgo != 1) {
                        printArray(array, j, j + 1);
                    }
                    // 교환 후 배열 출력0.5초대기

                    try {
                        Thread.sleep(5);

                    } catch (InterruptedException e) {
                        sm.getrunval(false);
                        Thread.currentThread().interrupt(); // 인터럽트 발생 시 현재 스레드 중단
                    }
                }

            }
            sm.getgreenc(array.length - (i + 1));

            // 내부 루프에서 두 요소가 교환되지 않았다면 배열이 정렬완료
            if (!swapped) {
                break;
            }

        }
        sm.getgreenc(0);
        sm.getredc(-1);
        playSound("START.WAV"); // 버블 끝
    }

    // 힙 정렬 알고리즘
    private void heapSort(int[] array) {
        int n = array.length;
        playSound("START.WAV"); // 힙 정렬 시작 소리 재생
        // 최대 힙 구성
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // 힙 정렬
        for (int i = n - 1; i >= 0; i--) {
            // 현재 루트를 끝으로 이동
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            sm.getredc(i);
            sm.getgreenc(i);

            if (allgo != 1) {
                printArray(array, 0, i);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                sm.getrunval(false);
                Thread.currentThread().interrupt(); // 인터럽트 발생 시 현재 스레드를 중단합니다.
            }

            // 루트 요소 힙 정리
            heapify(array, i, 0);
        }
        sm.getredc(-1);

        playSound("START.WAV"); // 힙 정렬 종료 소리 재생
    }

    // 주어진 인덱스의 서브트리를 힙으로 만드는 함수
    private void heapify(int[] arr, int n, int i) {
        int largest = i; // 최대값을 루트로 초기화
        int left = 2 * i + 1; // left = 2*i + 1
        int right = 2 * i + 2; // right = 2*i + 2
        sm.getcyanc(left);
        sm.getbluec(arr[i]);

        // 만약 왼쪽 자식이 루트보다 크다면
        if (left < n && arr[left] > arr[largest])
            largest = left;

        // 만약 오른쪽 자식이 현재까지 확인한 최대값보다 크다면
        if (right < n && arr[right] > arr[largest])
            largest = right;

        // 만약 최대값이 루트가 아니라면
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            if (allgo != 1) {
                printArray(arr, i, largest);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                sm.getrunval(false);
                Thread.currentThread().interrupt(); // 인터럽트 발생 시 현재 스레드를 중단합니다.
            }

            // 재귀적으로 힙 정리 실행
            heapify(arr, n, largest);
        }
    }

    // 배열이 정렬되었는지 확인하는 함수
    private boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }

    // 퀵 정렬 알고리즘
    private void quickSort(int[] array, int low, int high) {

        if (low < high) {
            // 파티셔닝 인덱스 계산
            int pi = partition(array, low, high);

            quickSort(array, low, pi - 1);
            if (low > sm.putgreenc()) {
                sm.getgreenc(low - 1);
            }

            quickSort(array, pi + 1, high);
        }

        sm.getgreenc(high - low);
        sm.getbluec(-1);
        sm.getcyanc(-1);
        sm.getredc(-1);

        // 정렬 작업 후 배열이 정렬되었는지 확인
        if (isSorted(array)) {
            //정렬이 완료되었으면 소리재생.
            playSound("START.WAV");
        }
    }

    private int partition(int[] array, int low, int high) {
        // 중간 인덱스 계산
        int mid = low + (high - low) / 2;

        // low, high, mid의 값을 가져옴
        int pivot = array[mid];

        int temp = array[high];
        array[high] = array[mid];
        array[mid] = temp;
        sm.getredc(pivot);

        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            if (array[j] <= pivot) {
                i++;
                sm.getbluec(i);
                // array[i] array[j] 교환
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                sm.getcyanc(j);
                if (i == pivot) {
                    sm.getredc(j);
                } else if (j == pivot) {
                    sm.getredc(i);
                }
                if (allgo != 1) {
                    printArray(array, i, j);
                }
                // 교환 배열출력
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    sm.getrunval(false);
                    Thread.currentThread().interrupt();
                }
            }
        }
        temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        if (allgo != 1) {
            printArray(array, i + 1, high);
        }
        // 교환 배열출력
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return (i + 1);
    }

    // 삽입 정렬 알고리즘
    private void insertionSort(int[] array) {
        playSound("START.WAV"); // 삽입시작

        for (int i = 1; i < array.length; i++) {
            sm.getcyanc(i);
            sm.getredc(i);

            int j;
           
            for (j = i - 1; j >= 0 && array[j] > array[j + 1]; j--) {

                sm.getredc(j + 1);
                int tmp = array[j + 1];
                array[j + 1] = array[j];
                array[j] = tmp;

                if (allgo != 1) {
                    printArray(array, j, j + 1);
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    sm.getrunval(false);
                    Thread.currentThread().interrupt();
                }

            }
            sm.getredc(j + 1);

            // array[j + 1] = key;

        }
        sm.getgreenc(array.length - 1);
        sm.getredc(-1);
        playSound("START.WAV");// 삽입 끝
    }

    // 선택 정렬 알고리즘
    private void selectionSort(int[] array) {
        playSound("START.WAV");

        int s = -1;
        for (int i = 0; i < array.length - 1; i++) {
            // Find the minimum element in unsorted array
            int minIdx = i;
            sm.getredc(minIdx);
            for (int j = i + 1; j < array.length; j++) {
                sm.getbluec(j);
                if (array[j] < array[minIdx]) {
                    minIdx = j;
                    sm.getredc(minIdx);
                }

            }

            // Swap the found minimum element with the first
            // element of the unsorted part
            if (minIdx != i) {
                int temp = array[minIdx];
                array[minIdx] = array[i];
                array[i] = temp;
            }

            sm.getgreenc(s++);
            if (allgo != 1) {
                printArray(array, i, minIdx);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                sm.getrunval(false);
                Thread.currentThread().interrupt(); // 인터럽트 발생 시 현재 스레드를 중단합니다.
            }
        }
        sm.getgreenc(s++);
        sm.getgreenc(s++);
        sm.getredc(-1);
        sm.getbluec(-1);
        playSound("START.WAV");
    }

    // 배열 출력
    private void printArray(int[] array, int index1, int index2) {
        StyledDocument doc = textPane.getStyledDocument();
        Style defaultStyle = textPane.getStyle(StyleContext.DEFAULT_STYLE);

        try {
            doc.remove(0, doc.getLength()); // 이전 내용을 모두 지웁니다.

            for (int i = 0; i < array.length; i++) {
                Style style = defaultStyle;
                if (i == index1 || i == index2) {
                    StyleConstants.setForeground(style, Color.RED);
                } else {
                    StyleConstants.setForeground(style, Color.BLACK);
                }

                doc.insertString(doc.getLength(), array[i] + " ", style);

                if ((i + 1) % 30 == 0) {
                    doc.insertString(doc.getLength(), "\n", defaultStyle);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    // JTextPane에 텍스트를 추가합니다.
    private void appendText(String text, AttributeSet attributes) {
        StyledDocument doc = textPane.getStyledDocument();

        try {
            doc.insertString(doc.getLength(), text, attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
