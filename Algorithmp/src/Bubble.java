import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Bubble implements Runnable {
	private int[] data;
	GraphPanel graphPanel = new GraphPanel();
	int redColumn;
	int greenColumn;
	int cyanColumn;
	int blueColumn;
	int allgo = 0;
	boolean runval = true;

	sharedmemory sm = new sharedmemory();

	public void stopThread() {
		runval = false;
	}

	@Override
	public void run() {
		//프레임창을 띄우고 while문을 통하여 변경값을 받아 계속 다시 그려줌.
		data = sm.putarray();
		allgo = sm.putallgo();
		Graph graph = new Graph();

		while (runval) {

			runval = sm.putrunval();
			if (!runval) {
				graph.setVisible(runval);
				graph.dispose();

				break;
			}
			redColumn = sm.putredc();
			greenColumn = sm.putgreenc();
			cyanColumn = sm.putcyanc();
			blueColumn = sm.putbluec();
			
			graphPanel.repaint();

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // 인터럽트 발생 시 현재 스레드를 중단합니다.
			}
		}

	}

	public Bubble(sharedmemory sm) {
		this.sm = sm;
		data = sm.putarray();
		redColumn = sm.putredc();
		greenColumn = sm.putgreenc();
	}

	public class Graph extends JFrame {

		public Graph() {

			if (allgo == 1) {
				setSize(sm.putsizex(), 500);
				setLocation(0, 0);

			} else {
				setSize(800, 500);
				int x = sm.putblocx();
				int y = sm.putblocy();
				setLocation(x, y);
			}
			setTitle("Bubble Sort Graph");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


			add(graphPanel);

			JLabel label = new JLabel("Bubble Sort");
			Font font = new Font("Serif", Font.BOLD, 25);
			label.setFont(font);
			graphPanel.setLayout(new BorderLayout());
			graphPanel.add(label, BorderLayout.NORTH);

			setVisible(runval);
			if (!runval) {
				dispose();
			}
		}

	}

	class GraphPanel extends JPanel {

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int BORDER_WIDTH = 10;
			int columnWidth = (getWidth() - 2 * BORDER_WIDTH) / data.length;
			int columnHeight = (getHeight() - 4 * BORDER_WIDTH) / data.length;

			for (int i = 0; i < (greenColumn == -1 ? data.length : greenColumn); i++) {
				g.setColor(Color.WHITE);
				g.fillRect(2 * BORDER_WIDTH + columnWidth * i, getHeight() - data[i] * columnHeight - 2 * BORDER_WIDTH,
						columnWidth, data[i] * columnHeight);
				g.setColor(Color.BLACK);
				g.drawRect(2 * BORDER_WIDTH + columnWidth * i, getHeight() - data[i] * columnHeight - 2 * BORDER_WIDTH,
						columnWidth, data[i] * columnHeight);
			}
			if (greenColumn != -1) {
				for (int i = greenColumn; i < data.length; i++) {
					g.setColor(Color.GREEN);
					g.fillRect(2 * BORDER_WIDTH + columnWidth * i,
							getHeight() - data[i] * columnHeight - 2 * BORDER_WIDTH, columnWidth,
							data[i] * columnHeight);
					g.setColor(Color.BLACK);
					g.drawRect(2 * BORDER_WIDTH + columnWidth * i,
							getHeight() - data[i] * columnHeight - 2 * BORDER_WIDTH, columnWidth,
							data[i] * columnHeight);
				}
			}
			if (redColumn != -1) {
				g.setColor(Color.RED);
				g.fillRect(2 * BORDER_WIDTH + columnWidth * redColumn,
						getHeight() - data[redColumn] * columnHeight - 2 * BORDER_WIDTH, columnWidth,
						data[redColumn] * columnHeight);
				g.setColor(Color.BLACK);
				g.drawRect(2 * BORDER_WIDTH + columnWidth * redColumn,
						getHeight() - data[redColumn] * columnHeight - 2 * BORDER_WIDTH, columnWidth,
						data[redColumn] * columnHeight);
			}

		}
	}
}
