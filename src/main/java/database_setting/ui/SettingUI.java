package database_setting.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import database_setting.service.ExportService;
import database_setting.service.ImportService;
import database_setting.service.InitService;

public class SettingUI extends JFrame implements ActionListener {
	private JPanel contentPane;
	private List<String> btnNames;
	private InitService initService;
	private ImportService importService;
	private ExportService exportService;

	public SettingUI() {
		btnNames = Arrays.asList("초기화", "백업", "복원");
		initService = new InitService();
		importService = new ImportService();
		exportService = new ExportService();

		initComponents();
	}

	private void initComponents() {
		setTitle("Database Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 10, 0));
		JPanel btnPannel = new JPanel();
		btnPannel.setBorder(new TitledBorder(null, "데이터베이스 관리 메뉴", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		contentPane.add(btnPannel);
		btnPannel.setLayout(new GridLayout(1, 0, 10, 0));
		for (String btnTitle : btnNames) {
			JButton btn = new JButton(btnTitle);
			btn.addActionListener(this);
			btnPannel.add(btn);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			switch (e.getActionCommand()) {
			case "초기화":
				initService.service(filePath("SQL File 디렉터리 선택", true));
				break;
			case "백업":
				exportService.service(filePath("백업 디렉터리 선택", true));
				break;
			case "복원":
				importService.service(filePath("DataFiles 디렉터리 선택", true));
				break;
			}
			JOptionPane.showMessageDialog(null, e.getActionCommand() + " 완료");
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
		}

	}

	private String filePath(String dialogTitle, boolean isOpen) throws Exception {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int ret = -1;
		if (isOpen)
			ret = chooser.showOpenDialog(null);
		else
			ret = chooser.showSaveDialog(null);
		if (ret != JFileChooser.APPROVE_OPTION) {
			throw new Exception("파일을 선택하지 않았습니다.");
		}
		return chooser.getSelectedFile().getPath();
	}

}
