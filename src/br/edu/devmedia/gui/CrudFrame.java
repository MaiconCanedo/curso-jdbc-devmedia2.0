package br.edu.devmedia.gui;

import br.edu.devmedia.bo.PessoaBO;
import br.edu.devmedia.dao.EnderecoDAO;
import br.edu.devmedia.dao.EstadoDAO;
import br.edu.devmedia.dao.PessoaDAO;
import br.edu.devmedia.dto.EnderecoDTO;
import br.edu.devmedia.dto.EstadoDTO;
import br.edu.devmedia.dto.PessoaDTO;
import br.edu.devmedia.dto.UsuarioDTO;
import br.edu.devmedia.exception.NegocioException;
import br.edu.devmedia.exception.PersistenciaException;
import br.edu.devmedia.exception.ValidacaoException;
import br.edu.devmedia.util.JTextFieldComTamanhoEspecifico;
import br.edu.devmedia.util.JTextFieldSomenteNumeros;
import br.edu.devmedia.util.MensagensUtil;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Maicon
 */
public class CrudFrame extends javax.swing.JFrame {

    private DefaultTableModel modeloPrincipal = new DefaultTableModel();
    private DefaultTableModel modeloExcluido = new DefaultTableModel();
    private DefaultTableModel modeloConsulta = new DefaultTableModel();

    private PessoaBO pessoaBO = new PessoaBO();
    private PessoaDTO pessoaDTO;
    private EnderecoDTO enderecoDTO;
    private EnderecoDAO enderecoDAO;
    private EstadoDAO estadoDAO = new EstadoDAO();
    private EstadoDTO estadoDTO;

    private StringBuilder msg = new StringBuilder();
    private int[] lista = null;
    private boolean carregar = true;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //private TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloPrincipal);

    private Long idPessoa;
    private String nome, cpf;
    private Character sexo;
    private Date dataNascimento;

    private Integer numero, cep;
    private String logradouro, bairro;

    //private  PainelEndereco painelEndereco;
    public CrudFrame(UsuarioDTO usuarioDTO) {
        initComponents();

        pgPrincipal.remove(pEndereco);

        modeloPrincipal = (DefaultTableModel) tbPrincipal.getModel();
        modeloExcluido = (DefaultTableModel) tbExcluidos.getModel();
        modeloConsulta = (DefaultTableModel) tbConsulta.getModel();

        ctNome.setDocument(new JTextFieldComTamanhoEspecifico(45));
        ctBairro.setDocument(new JTextFieldComTamanhoEspecifico(45));
        ctLogradouro.setDocument(new JTextFieldComTamanhoEspecifico(150));
        ctNumero.setDocument(new JTextFieldSomenteNumeros(10));
        ctNomeConsulta.setDocument(new JTextFieldComTamanhoEspecifico(45));
        ctCpfConsulta.setDocument(new JTextFieldSomenteNumeros(11));

        alimentarCbEstado();
        
        try {
            if (usuarioDTO.getAtivo() == 1) {
                if (usuarioDTO.getTipo() != 1){
                    pgPrincipal.remove(pExcluidos);
                }
            } else{
                MensagensUtil.msgErr(this, "Esse usuário está inativo!", "Você está inativo!");
                System.exit(0);
            }
            setTitle(getTitle() + " - " + usuarioDTO.getNome());
        } catch (NullPointerException e) {
            MensagensUtil.msgErr(this, "Nenhum usuário foi informado!\nSem logar não poderá acessar", "Quem é você?!");
            System.exit(0);
        }
        //painelEndereco = new PainelEndereco(pgPrincipal, pCadastro);
        //tbPrincipal.setRowSorter(sorter);
    }

    private void alimentarVariaveis() throws ParseException, PersistenciaException {
        logradouro = ctLogradouro.getText().trim().length() > 4 ? ctLogradouro.getText().trim() : null;
        bairro = ctBairro.getText().trim().length() > 2 ? ctBairro.getText().trim() : null;
        numero = ctNumero.getText().length() > 0 ? Integer.valueOf(ctNumero.getText()) : null;
        cep = ctCep.getText().trim().replace("-", "").length() == 8 ? Integer.valueOf(ctCep.getText().replace("-", "")) : null;
        estadoDTO = cbEstado.getSelectedIndex() > 0 ? estadoDAO.getByDescricao(cbEstado.getSelectedItem().toString()) : null;

        nome = ctNome.getText().trim().length() > 2 ? ctNome.getText().trim() : null;
        cpf = ctCpf.getText().trim().length() == 14 ? ctCpf.getText().trim() : null;
        sexo = bgSexo.getSelection().getActionCommand().charAt(0);
        dataNascimento = ctDataNascimento.getText().trim().length() == 10 ? dateFormat.parse(ctDataNascimento.getText()) : null;
    }

    private void carregarTabelaPrincipal() {
        try {
            modeloPrincipal.setNumRows(0);
            for (PessoaDTO pessoaDTO : pessoaBO.listar(1)) {
                modeloPrincipal.addRow(pessoaDTO.getArrayString());
            }
            carregar = false;
            ativaDesativaBotoes(carregar);
        } catch (PersistenciaException ex) {
            MensagensUtil.msgErr(this, ex.getMessage(), "Erro ao listar!");
        }
    }

    private void carregarTabelaExcluidos() {
        try {
            modeloExcluido.setNumRows(0);
            for (PessoaDTO pessoaDTO : pessoaBO.listar(0)) {
                modeloExcluido.addRow(pessoaDTO.getArrayString());
            }
            ativaDesativaBotoesExcluidos(false);
        } catch (PersistenciaException ex) {
            MensagensUtil.msgErr(this, ex.getMessage(), "Erro ao listar!");
        }
    }

    private void restaurarExcluido() {
        try {
            for (int i = 0; i < lista.length; i++) {
                pessoaBO.ativarDesativar(Long.valueOf(tbExcluidos.getValueAt(lista[i], 0).toString()), 1);
            }
            MensagensUtil.msgInfo(this, "Item restaurado com sucesso!", "Restauração bem sucedida!");
            carregarTabelaExcluidos();
        } catch (Exception ex) {
            MensagensUtil.msgErr(this, ex.getMessage(), "Erro!");
        }
    }

    private void desativar() {
        try {
            for (int i = 0; i < lista.length; i++) {
                pessoaBO.ativarDesativar(Long.valueOf(tbPrincipal.getValueAt(lista[i], 0).toString()), 0);
            }
            MensagensUtil.msgInfo(this, "Item(s) excluido(s) com sucesso!", "Exclusão bem sucedida!");
            pListaComponentShown(null);
        } catch (Exception ex) {
            MensagensUtil.msgErr(this, ex.getMessage(), "Erro!");
        }
    }

    private void excluir() throws PersistenciaException {
        for (int i = 0; i < lista.length; i++) {
            pessoaBO.excluir(Long.valueOf(tbExcluidos.getValueAt(lista[i], 0).toString()));
        }
        MensagensUtil.msgInfo(this, "Item(s) excluido(s) permanentemente com sucesso!", "Exclusão permanente bem sucedida!");
        carregarTabelaExcluidos();
    }

    private void alimentarCbEstado() {
        try {
            //cbEstado.removeAllItems();
            for (EstadoDTO estadoDTO : estadoDAO.listarTodos()) {
                cbEstado.addItem(estadoDTO.getDescricao());
                cbEstadoConsulta.addItem(estadoDTO.getDescricao());
            }
        } catch (Exception e) {
            MensagensUtil.msgErr(this, "Erro ao carregar estados:\n" + e.getMessage(), "Erro ao carregar estados!");
        }
    }

    private void ativaDesativaBotoes(Boolean ativo) {
        btLimparSelecao.setEnabled(ativo);
        btDesativar.setEnabled(ativo);
    }

    private void ativaDesativaBotoesExcluidos(Boolean ativo) {
        btRestaurar.setEnabled(ativo);
        btExcluir.setEnabled(ativo);
    }

    private void campoValido(JLabel jLabel, boolean valido) {
        if (valido) {
            jLabel.setText("✔");
            jLabel.setForeground(new Color(0, 153, 0));
        } else {
            jLabel.setText("X");
            jLabel.setForeground(new Color(204, 0, 0));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgSexo = new javax.swing.ButtonGroup();
        bgSexoConsulta = new javax.swing.ButtonGroup();
        bgOrdem = new javax.swing.ButtonGroup();
        jLabel16 = new javax.swing.JLabel();
        pgPrincipal = new javax.swing.JTabbedPane();
        pCadastro = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ctNome = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        brMasculino = new javax.swing.JRadioButton();
        brFeminino = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ctDataNascimento = new javax.swing.JFormattedTextField();
        btLimpar = new javax.swing.JButton();
        ctCpf = new javax.swing.JFormattedTextField();
        btNext = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lbNome = new javax.swing.JLabel();
        lbCpf = new javax.swing.JLabel();
        lbDataNascimento = new javax.swing.JLabel();
        pLista = new javax.swing.JPanel();
        spLista = new javax.swing.JScrollPane();
        tbPrincipal = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btRecarregar = new javax.swing.JButton();
        btDesativar = new javax.swing.JButton();
        btLimparSelecao = new javax.swing.JButton();
        pConsulta = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        ctNomeConsulta = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        brFemininoConsulta = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        brMasculinoConsulta = new javax.swing.JRadioButton();
        btConsultar = new javax.swing.JButton();
        brNome = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        brCpf = new javax.swing.JRadioButton();
        ctCpfConsulta = new javax.swing.JTextField();
        cbEstadoConsulta = new javax.swing.JComboBox<>();
        spLista3 = new javax.swing.JScrollPane();
        tbConsulta = new javax.swing.JTable();
        pExcluidos = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btRestaurar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btRecarregarExcluidos = new javax.swing.JButton();
        spLista2 = new javax.swing.JScrollPane();
        tbExcluidos = new javax.swing.JTable();
        pEndereco = new javax.swing.JPanel();
        ctCep = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        cbEstado = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        ctLogradouro = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ctNumero = new javax.swing.JTextField();
        btVoltar = new javax.swing.JButton();
        ctBairro = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        btCadastrarAtualizar = new javax.swing.JButton();
        btLimparEndereco = new javax.swing.JButton();
        lbLogradouro = new javax.swing.JLabel();
        lbNumero = new javax.swing.JLabel();
        lbCep = new javax.swing.JLabel();
        lbBairro = new javax.swing.JLabel();

        jLabel16.setText("jLabel16");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tela Principal");
        setMinimumSize(new java.awt.Dimension(320, 300));
        setPreferredSize(new java.awt.Dimension(650, 552));

        pgPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        pgPrincipal.setOpaque(true);

        pCadastro.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Nome:");

        ctNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ctNomeKeyReleased(evt);
            }
        });

        jLabel2.setText("CPF:");

        brMasculino.setBackground(new java.awt.Color(255, 255, 255));
        bgSexo.add(brMasculino);
        brMasculino.setText("Masculino");
        brMasculino.setToolTipText("");
        brMasculino.setActionCommand("M");

        brFeminino.setBackground(new java.awt.Color(255, 255, 255));
        bgSexo.add(brFeminino);
        brFeminino.setSelected(true);
        brFeminino.setText("Feminino");
        brFeminino.setToolTipText("");
        brFeminino.setActionCommand("F");
        brFeminino.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setText("Sexo:");

        jLabel4.setText("Data de Nascimento:");

        try {
            ctDataNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ctDataNascimento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ctDataNascimentoKeyReleased(evt);
            }
        });

        btLimpar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btLimpar.setMnemonic('l');
        btLimpar.setText("Limpar");
        btLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimparActionPerformed(evt);
            }
        });

        try {
            ctCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ctCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ctCpfKeyReleased(evt);
            }
        });

        btNext.setMnemonic('c');
        btNext.setText("Continuar");
        btNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNextActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel15.setText("Passo 1/");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(153, 153, 153));
        jLabel17.setText("Passo 2");

        lbNome.setBackground(new java.awt.Color(255, 255, 255));
        lbNome.setFont(new java.awt.Font("Segoe UI Symbol", 0, 11)); // NOI18N
        lbNome.setForeground(new java.awt.Color(0, 153, 0));

        lbCpf.setFont(new java.awt.Font("Segoe UI Symbol", 0, 11)); // NOI18N
        lbCpf.setForeground(new java.awt.Color(204, 0, 0));

        lbDataNascimento.setFont(new java.awt.Font("Segoe UI Symbol", 0, 11)); // NOI18N

        javax.swing.GroupLayout pCadastroLayout = new javax.swing.GroupLayout(pCadastro);
        pCadastro.setLayout(pCadastroLayout);
        pCadastroLayout.setHorizontalGroup(
            pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCadastroLayout.createSequentialGroup()
                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pCadastroLayout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pCadastroLayout.createSequentialGroup()
                                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ctNome)
                                    .addComponent(ctCpf)))
                            .addGroup(pCadastroLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(brFeminino)
                                .addGap(18, 18, 18)
                                .addComponent(brMasculino))
                            .addGroup(pCadastroLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel4)
                                .addGap(43, 43, 43)
                                .addComponent(ctDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbNome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pCadastroLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)))
                .addContainerGap(131, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pCadastroLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btLimpar)
                    .addComponent(btNext))
                .addGap(31, 31, 31))
        );
        pCadastroLayout.setVerticalGroup(
            pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCadastroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17))
                .addGap(72, 72, 72)
                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ctNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbNome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ctCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brMasculino)
                    .addComponent(jLabel3)
                    .addComponent(brFeminino))
                .addGap(18, 18, 18)
                .addGroup(pCadastroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ctDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(btLimpar)
                .addGap(51, 51, 51)
                .addComponent(btNext)
                .addGap(26, 26, 26))
        );

        brMasculino.getAccessibleContext().setAccessibleDescription("m");
        brFeminino.getAccessibleContext().setAccessibleDescription("f");

        pgPrincipal.addTab("Cadastro", pCadastro);

        pLista.setBackground(new java.awt.Color(255, 255, 255));
        pLista.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pListaComponentShown(evt);
            }
        });

        tbPrincipal.setAutoCreateRowSorter(true);
        tbPrincipal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbPrincipal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Nome", "Sexo", "CPF", "Endereco", "cep", "Data de Nascimento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbPrincipal.setAlignmentX(0.8F);
        tbPrincipal.setAlignmentY(0.8F);
        tbPrincipal.setIntercellSpacing(new java.awt.Dimension(3, 3));
        tbPrincipal.setRowHeight(20);
        tbPrincipal.getTableHeader().setReorderingAllowed(false);
        tbPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPrincipalMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbPrincipalMouseReleased(evt);
            }
        });
        spLista.setViewportView(tbPrincipal);
        if (tbPrincipal.getColumnModel().getColumnCount() > 0) {
            tbPrincipal.getColumnModel().getColumn(0).setMinWidth(5);
            tbPrincipal.getColumnModel().getColumn(0).setPreferredWidth(25);
            tbPrincipal.getColumnModel().getColumn(0).setMaxWidth(40);
            tbPrincipal.getColumnModel().getColumn(1).setMinWidth(5);
            tbPrincipal.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbPrincipal.getColumnModel().getColumn(1).setMaxWidth(500);
            tbPrincipal.getColumnModel().getColumn(2).setMinWidth(5);
            tbPrincipal.getColumnModel().getColumn(2).setPreferredWidth(34);
            tbPrincipal.getColumnModel().getColumn(2).setMaxWidth(35);
            tbPrincipal.getColumnModel().getColumn(3).setMinWidth(5);
            tbPrincipal.getColumnModel().getColumn(3).setPreferredWidth(108);
            tbPrincipal.getColumnModel().getColumn(3).setMaxWidth(112);
            tbPrincipal.getColumnModel().getColumn(5).setMinWidth(5);
            tbPrincipal.getColumnModel().getColumn(5).setPreferredWidth(69);
            tbPrincipal.getColumnModel().getColumn(5).setMaxWidth(80);
            tbPrincipal.getColumnModel().getColumn(6).setMinWidth(5);
            tbPrincipal.getColumnModel().getColumn(6).setPreferredWidth(106);
            tbPrincipal.getColumnModel().getColumn(6).setMaxWidth(107);
        }

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btRecarregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/devmedia/img/atualizar.png"))); // NOI18N
        btRecarregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRecarregarActionPerformed(evt);
            }
        });

        btDesativar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btDesativar.setMnemonic('e');
        btDesativar.setText("Excluir");
        btDesativar.setEnabled(false);
        btDesativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDesativarActionPerformed(evt);
            }
        });

        btLimparSelecao.setText("Limpar Selecção");
        btLimparSelecao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimparSelecaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btRecarregar)
                .addGap(18, 18, 18)
                .addComponent(btLimparSelecao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btDesativar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btDesativar)
                        .addComponent(btLimparSelecao))
                    .addComponent(btRecarregar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pListaLayout = new javax.swing.GroupLayout(pLista);
        pLista.setLayout(pListaLayout);
        pListaLayout.setHorizontalGroup(
            pListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pListaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spLista, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pListaLayout.setVerticalGroup(
            pListaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pListaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spLista, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pgPrincipal.addTab("Listagem", pLista);

        pConsulta.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Nome:");

        jLabel8.setText("Sexo:");

        brFemininoConsulta.setBackground(new java.awt.Color(255, 255, 255));
        bgSexoConsulta.add(brFemininoConsulta);
        brFemininoConsulta.setSelected(true);
        brFemininoConsulta.setText("Feminino");
        brFemininoConsulta.setToolTipText("");
        brFemininoConsulta.setActionCommand("F");
        brFemininoConsulta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel7.setText("CPF:");

        brMasculinoConsulta.setBackground(new java.awt.Color(255, 255, 255));
        bgSexoConsulta.add(brMasculinoConsulta);
        brMasculinoConsulta.setText("Masculino");
        brMasculinoConsulta.setToolTipText("");
        brMasculinoConsulta.setActionCommand("M");

        btConsultar.setMnemonic('c');
        btConsultar.setText("Consultar");
        btConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConsultarActionPerformed(evt);
            }
        });

        brNome.setBackground(new java.awt.Color(255, 255, 255));
        bgOrdem.add(brNome);
        brNome.setSelected(true);
        brNome.setText("Nome");
        brNome.setToolTipText("");
        brNome.setActionCommand("nome");
        brNome.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel9.setText("Ordenar por:");

        brCpf.setBackground(new java.awt.Color(255, 255, 255));
        bgOrdem.add(brCpf);
        brCpf.setText("CPF");
        brCpf.setToolTipText("");
        brCpf.setActionCommand("cpf");

        cbEstadoConsulta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Selecione um estado --" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ctNomeConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ctCpfConsulta))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(brNome)
                                .addGap(18, 18, 18)
                                .addComponent(brCpf))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(brFemininoConsulta)
                                .addGap(18, 18, 18)
                                .addComponent(brMasculinoConsulta)))
                        .addGap(84, 84, 84)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btConsultar)
                            .addComponent(cbEstadoConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ctNomeConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(ctCpfConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brMasculinoConsulta)
                    .addComponent(jLabel8)
                    .addComponent(brFemininoConsulta)
                    .addComponent(cbEstadoConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brCpf)
                    .addComponent(jLabel9)
                    .addComponent(brNome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(btConsultar)
                .addContainerGap())
        );

        tbConsulta.setAutoCreateRowSorter(true);
        tbConsulta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Nome", "Sexo", "CPF", "Endereco", "cep", "Data de Nascimento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbConsulta.setAlignmentX(0.8F);
        tbConsulta.setAlignmentY(0.8F);
        tbConsulta.setIntercellSpacing(new java.awt.Dimension(3, 3));
        tbConsulta.setRowHeight(20);
        tbConsulta.getTableHeader().setReorderingAllowed(false);
        spLista3.setViewportView(tbConsulta);
        if (tbConsulta.getColumnModel().getColumnCount() > 0) {
            tbConsulta.getColumnModel().getColumn(0).setMinWidth(5);
            tbConsulta.getColumnModel().getColumn(0).setPreferredWidth(25);
            tbConsulta.getColumnModel().getColumn(0).setMaxWidth(40);
            tbConsulta.getColumnModel().getColumn(1).setMinWidth(5);
            tbConsulta.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbConsulta.getColumnModel().getColumn(1).setMaxWidth(500);
            tbConsulta.getColumnModel().getColumn(2).setMinWidth(5);
            tbConsulta.getColumnModel().getColumn(2).setPreferredWidth(34);
            tbConsulta.getColumnModel().getColumn(2).setMaxWidth(35);
            tbConsulta.getColumnModel().getColumn(3).setMinWidth(5);
            tbConsulta.getColumnModel().getColumn(3).setPreferredWidth(109);
            tbConsulta.getColumnModel().getColumn(3).setMaxWidth(112);
            tbConsulta.getColumnModel().getColumn(5).setMinWidth(5);
            tbConsulta.getColumnModel().getColumn(5).setPreferredWidth(69);
            tbConsulta.getColumnModel().getColumn(5).setMaxWidth(80);
            tbConsulta.getColumnModel().getColumn(6).setMinWidth(5);
            tbConsulta.getColumnModel().getColumn(6).setPreferredWidth(106);
            tbConsulta.getColumnModel().getColumn(6).setMaxWidth(107);
        }

        javax.swing.GroupLayout pConsultaLayout = new javax.swing.GroupLayout(pConsulta);
        pConsulta.setLayout(pConsultaLayout);
        pConsultaLayout.setHorizontalGroup(
            pConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spLista3, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
        );
        pConsultaLayout.setVerticalGroup(
            pConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spLista3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pgPrincipal.addTab("Consulta", pConsulta);

        pExcluidos.setBackground(new java.awt.Color(204, 204, 204));
        pExcluidos.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pExcluidosComponentShown(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        btRestaurar.setBackground(new java.awt.Color(204, 204, 204));
        btRestaurar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btRestaurar.setMnemonic('r');
        btRestaurar.setText("Restaurar");
        btRestaurar.setEnabled(false);
        btRestaurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRestaurarActionPerformed(evt);
            }
        });

        btExcluir.setBackground(new java.awt.Color(204, 204, 204));
        btExcluir.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btExcluir.setText("Excluir");
        btExcluir.setEnabled(false);
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btRecarregarExcluidos.setBackground(new java.awt.Color(204, 204, 204));
        btRecarregarExcluidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/edu/devmedia/img/atualizar.png"))); // NOI18N
        btRecarregarExcluidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRecarregarExcluidosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btRecarregarExcluidos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRestaurar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 366, Short.MAX_VALUE)
                .addComponent(btExcluir)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btRestaurar)
                        .addComponent(btExcluir))
                    .addComponent(btRecarregarExcluidos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        tbExcluidos.setAutoCreateRowSorter(true);
        tbExcluidos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbExcluidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Nome", "Sexo", "CPF", "Endereco", "cep", "Data de Nascimento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbExcluidos.setAlignmentX(0.8F);
        tbExcluidos.setAlignmentY(0.8F);
        tbExcluidos.setIntercellSpacing(new java.awt.Dimension(3, 3));
        tbExcluidos.setRowHeight(20);
        tbExcluidos.getTableHeader().setReorderingAllowed(false);
        tbExcluidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbExcluidosMouseReleased(evt);
            }
        });
        spLista2.setViewportView(tbExcluidos);
        if (tbExcluidos.getColumnModel().getColumnCount() > 0) {
            tbExcluidos.getColumnModel().getColumn(0).setMinWidth(5);
            tbExcluidos.getColumnModel().getColumn(0).setPreferredWidth(25);
            tbExcluidos.getColumnModel().getColumn(0).setMaxWidth(40);
            tbExcluidos.getColumnModel().getColumn(1).setMinWidth(5);
            tbExcluidos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbExcluidos.getColumnModel().getColumn(1).setMaxWidth(500);
            tbExcluidos.getColumnModel().getColumn(2).setMinWidth(5);
            tbExcluidos.getColumnModel().getColumn(2).setPreferredWidth(34);
            tbExcluidos.getColumnModel().getColumn(2).setMaxWidth(35);
            tbExcluidos.getColumnModel().getColumn(3).setMinWidth(5);
            tbExcluidos.getColumnModel().getColumn(3).setPreferredWidth(109);
            tbExcluidos.getColumnModel().getColumn(3).setMaxWidth(112);
            tbExcluidos.getColumnModel().getColumn(5).setMinWidth(5);
            tbExcluidos.getColumnModel().getColumn(5).setPreferredWidth(69);
            tbExcluidos.getColumnModel().getColumn(5).setMaxWidth(80);
            tbExcluidos.getColumnModel().getColumn(6).setMinWidth(5);
            tbExcluidos.getColumnModel().getColumn(6).setPreferredWidth(106);
            tbExcluidos.getColumnModel().getColumn(6).setMaxWidth(107);
        }

        javax.swing.GroupLayout pExcluidosLayout = new javax.swing.GroupLayout(pExcluidos);
        pExcluidos.setLayout(pExcluidosLayout);
        pExcluidosLayout.setHorizontalGroup(
            pExcluidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pExcluidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spLista2, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
        );
        pExcluidosLayout.setVerticalGroup(
            pExcluidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pExcluidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spLista2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pgPrincipal.addTab("Pessoas Excluidas", pExcluidos);

        pEndereco.setBackground(new java.awt.Color(255, 255, 255));

        try {
            ctCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ctCep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ctCepKeyReleased(evt);
            }
        });

        jLabel10.setText("Estado:");

        cbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Selecione um estado --" }));

        jLabel11.setText("Logradouro:");

        ctLogradouro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ctLogradouroKeyReleased(evt);
            }
        });

        jLabel12.setText("Bairro:");

        jLabel13.setText("Numero:");

        ctNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ctNumeroKeyReleased(evt);
            }
        });

        btVoltar.setText("Voltar");
        btVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btVoltarActionPerformed(evt);
            }
        });

        ctBairro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ctBairroKeyReleased(evt);
            }
        });

        jLabel14.setText("CEP:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(153, 153, 153));
        jLabel18.setText("Passo 1/");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel19.setText("Passo 2");

        btCadastrarAtualizar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btCadastrarAtualizar.setMnemonic('c');
        btCadastrarAtualizar.setText("Cadastrar/Atualizar");
        btCadastrarAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCadastrarAtualizarActionPerformed(evt);
            }
        });

        btLimparEndereco.setText("Limpar");
        btLimparEndereco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimparEnderecoActionPerformed(evt);
            }
        });

        lbLogradouro.setBackground(new java.awt.Color(255, 255, 255));
        lbLogradouro.setFont(new java.awt.Font("Segoe UI Symbol", 0, 11)); // NOI18N
        lbLogradouro.setForeground(new java.awt.Color(0, 153, 0));

        lbNumero.setBackground(new java.awt.Color(255, 255, 255));
        lbNumero.setFont(new java.awt.Font("Segoe UI Symbol", 0, 11)); // NOI18N
        lbNumero.setForeground(new java.awt.Color(0, 153, 0));

        lbCep.setBackground(new java.awt.Color(255, 255, 255));
        lbCep.setFont(new java.awt.Font("Segoe UI Symbol", 0, 11)); // NOI18N
        lbCep.setForeground(new java.awt.Color(0, 153, 0));

        lbBairro.setBackground(new java.awt.Color(255, 255, 255));
        lbBairro.setFont(new java.awt.Font("Segoe UI Symbol", 0, 11)); // NOI18N
        lbBairro.setForeground(new java.awt.Color(0, 153, 0));

        javax.swing.GroupLayout pEnderecoLayout = new javax.swing.GroupLayout(pEndereco);
        pEndereco.setLayout(pEnderecoLayout);
        pEnderecoLayout.setHorizontalGroup(
            pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pEnderecoLayout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pEnderecoLayout.createSequentialGroup()
                        .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pEnderecoLayout.createSequentialGroup()
                                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ctLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pEnderecoLayout.createSequentialGroup()
                                        .addComponent(ctNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pEnderecoLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ctBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pEnderecoLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pEnderecoLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ctCep, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbCep, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(148, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pEnderecoLayout.createSequentialGroup()
                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pEnderecoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btLimparEndereco))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pEnderecoLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pEnderecoLayout.createSequentialGroup()
                                .addComponent(btVoltar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btCadastrarAtualizar)))))
                .addGap(25, 25, 25))
        );
        pEnderecoLayout.setVerticalGroup(
            pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pEnderecoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addGap(85, 85, 85)
                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pEnderecoLayout.createSequentialGroup()
                        .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pEnderecoLayout.createSequentialGroup()
                                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pEnderecoLayout.createSequentialGroup()
                                        .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel11)
                                                .addComponent(ctLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(lbLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel13)
                                            .addComponent(ctNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lbNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(ctCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lbCep, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(ctBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lbBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(btLimparEndereco)
                .addGap(48, 48, 48)
                .addGroup(pEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btVoltar)
                    .addComponent(btCadastrarAtualizar))
                .addGap(25, 25, 25))
        );

        pgPrincipal.addTab("Endereço", pEndereco);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pgPrincipal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pgPrincipal)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimparActionPerformed
        ctNome.setText(null);
        ctCpf.setText(null);
        ctDataNascimento.setText(null);
        brFeminino.setSelected(true);

        idPessoa = null;
        nome = null;
        cpf = null;
        sexo = null;
        dataNascimento = null;

        lbNome.setText(null);
        lbCpf.setText(null);
        lbDataNascimento.setText(null);
    }//GEN-LAST:event_btLimparActionPerformed

    private void pListaComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pListaComponentShown
        if (carregar) {
            carregarTabelaPrincipal();
        }
    }//GEN-LAST:event_pListaComponentShown

    private void btRecarregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRecarregarActionPerformed
        carregarTabelaPrincipal();
    }//GEN-LAST:event_btRecarregarActionPerformed

    private void btDesativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDesativarActionPerformed
        if (tbPrincipal.getSelectedRowCount() > 0) {
            msg.setLength(0);
            lista = tbPrincipal.getSelectedRows();
            for (int i = 0; i < lista.length; i++) {
                msg.append("\n" + tbPrincipal.getValueAt(lista[i], 1));
            }
            if (MensagensUtil.confirDiag(this, "Você deseja realmente excluir o(s) seguinte(s) intem(s)?" + msg, "Excluir?", MensagensUtil.SIM_NAO) == 0) {
                desativar();
                modeloPrincipal.removeRow(tbPrincipal.getSelectedRow());
                ativaDesativaBotoes(false);
            }
        } else {
            MensagensUtil.msgErr(this, "Nenhuma linha selecionada!", "Erro 404");
        }
    }//GEN-LAST:event_btDesativarActionPerformed

    private void btRestaurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRestaurarActionPerformed
        if (tbExcluidos.getSelectedRowCount() > 0) {
            msg.setLength(0);
            lista = tbExcluidos.getSelectedRows();
            for (int i = 0; i < lista.length; i++) {
                msg.append("\n" + tbExcluidos.getValueAt(lista[i], 1));
            }
            if (MensagensUtil.confirDiag(this, "Você deseja realmente restaurar o(s) seginte(s) item(s)?" + msg, "Excluir?", MensagensUtil.SIM_NAO) == 0) {
                restaurarExcluido();
            }
        } else {
            MensagensUtil.msgErr(this, "Nenhuma linha selecionada!", "Erro 404");
        }
    }//GEN-LAST:event_btRestaurarActionPerformed

    private void btRecarregarExcluidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRecarregarExcluidosActionPerformed
        carregarTabelaExcluidos();
    }//GEN-LAST:event_btRecarregarExcluidosActionPerformed

    private void btConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConsultarActionPerformed
        try {
            modeloConsulta.setNumRows(0);

            String nome = ctNomeConsulta.getText().trim().length() > 0 ? ctNomeConsulta.getText().trim() : null;
            String cpf = ctCpfConsulta.getText().replaceAll("[^0-9]", "").length() > 0 ? ctCpfConsulta.getText().replaceAll("[^0-9]", "") : null;
            String sexo = bgSexoConsulta.getSelection().getActionCommand().length() > 0 ? bgSexoConsulta.getSelection().getActionCommand() : null;

            for (PessoaDTO pessoaDTO : pessoaBO.filtrarPessoa(nome, cpf, sexo, bgOrdem.getSelection().getActionCommand())) {
                modeloConsulta.addRow(pessoaDTO.getArrayString());
            }
        } catch (Exception ex) {
            MensagensUtil.msgErr(this, ex.getMessage(), "Erro ao listar");
        }
    }//GEN-LAST:event_btConsultarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        if (tbExcluidos.getSelectedRowCount() > 0) {
            msg.setLength(0);
            lista = tbExcluidos.getSelectedRows();
            for (int i = 0; i < lista.length; i++) {
                msg.append("\n" + tbExcluidos.getValueAt(lista[i], 1));
            }
            if (MensagensUtil.confirDiag(this, "Você deseja realmente excluir permanetemente o(s) seginte(s) item(s)?" + msg, "Excluir Para Sempre?", MensagensUtil.SIM_NAO) == 0) {
                try {
                    excluir();
                    ativaDesativaBotoesExcluidos(false);
                } catch (PersistenciaException ex) {
                    MensagensUtil.msgErr(this, ex.getMessage(), "Erro ao excluir");
                }
            }
        } else {
            MensagensUtil.msgErr(this, "Nenhuma linha selecionada!", "Erro 404");
        }
    }//GEN-LAST:event_btExcluirActionPerformed

    private void tbPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPrincipalMouseClicked
        if (evt.getClickCount() == 2) {
            if (tbPrincipal.getSelectedRowCount() > 0) {
                try {
                    btLimparActionPerformed(null);
                    btLimparEnderecoActionPerformed(null);
                    enderecoDTO = null;
                    pessoaDTO = null;

                    pessoaDTO = pessoaBO.getById(Long.valueOf(tbPrincipal.getValueAt(tbPrincipal.getSelectedRow(), 0).toString()));
                    idPessoa = pessoaDTO.getIdPessoa();
                    ctNome.setText(pessoaDTO.getNome());
                    ctCpf.setText(pessoaDTO.getCpf().toString());
                    ctDataNascimento.setText(dateFormat.format(pessoaDTO.getDataNascimento()));
                    bgSexo.setSelected(pessoaDTO.getSexo() == 'F' ? brFeminino.getModel() : brMasculino.getModel(), true);

                    enderecoDTO = pessoaDTO.getEndereco();
                    if (enderecoDTO != null) {
                        ctLogradouro.setText(enderecoDTO.getLogradouro());
                        ctBairro.setText(enderecoDTO.getBairro());
                        ctCep.setText(enderecoDTO.getCep().toString());
                        ctNumero.setText(enderecoDTO.getNumero().toString());
                        cbEstado.setSelectedItem(enderecoDTO.getEstado().getDescricao());
                    }

                    btVoltarActionPerformed(null);
                } catch (PersistenciaException ex) {
                    MensagensUtil.msgErr(this, "Erro ao copiar dados", "Erro!");
                }
            } else {
                MensagensUtil.msgErr(this, "Nenhuma linha selecionada!", "Erro 404");
            }
        }
    }//GEN-LAST:event_tbPrincipalMouseClicked

    private void btNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextActionPerformed
        pgPrincipal.add(pEndereco, "Endereço", 0);
        pgPrincipal.setSelectedComponent(pEndereco);
        pgPrincipal.remove(pCadastro);
    }//GEN-LAST:event_btNextActionPerformed

    private void btVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btVoltarActionPerformed
        pgPrincipal.add(pCadastro, "Endereço", 0);
        pgPrincipal.setSelectedComponent(pCadastro);
        pgPrincipal.remove(pEndereco);
    }//GEN-LAST:event_btVoltarActionPerformed

    private void btCadastrarAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCadastrarAtualizarActionPerformed
        try {
            alimentarVariaveis();
            enderecoDTO = new EnderecoDTO(cep, numero, idPessoa, logradouro, bairro, estadoDTO);
            pessoaDTO = new PessoaDTO(idPessoa, nome, enderecoDTO, cpf, sexo, dataNascimento);

            if (pessoaDTO.getIdPessoa() == null) {
                enderecoDTO.setIdEndereco(pessoaBO.cadastrar(enderecoDTO, pessoaDTO));
                pessoaDTO.setEndereco(enderecoDTO);
                pessoaDTO.setIdPessoa(pessoaBO.cadastrar(pessoaDTO));
                modeloPrincipal.addRow(pessoaDTO.getArrayString());
                MensagensUtil.msgInfo(this, (sexo == 'F' ? "A " : "O ") + nome + " foi cadastrad" + (sexo == 'F' ? "a " : "o ") + "com sucesso!", "Cadastro bem sucedido!");
            } else {
                if (MensagensUtil.confirDiag(this, "Você deseja atualizar " + nome + "?", "Atualizar?", MensagensUtil.SIM_NAO) == 0) {
                    if (enderecoDTO.getIdEndereco() == null) {
                        enderecoDTO.setIdEndereco(pessoaBO.cadastrar(enderecoDTO, pessoaDTO));
                        pessoaDTO.setEndereco(enderecoDTO);
                    } else {
                        enderecoDAO.atualizar(enderecoDTO);
                    }
                    pessoaBO.atualizar(pessoaDTO);
                    //modeloPrincipal.addRow(pessoaDTO.getArrayString());
                    //modeloPrincipal.removeRow(tbPrincipal.getSelectedRow());
                    modeloPrincipal.setValueAt(nome, tbPrincipal.getSelectedRow(), 1);
                    modeloPrincipal.setValueAt(sexo, tbPrincipal.getSelectedRow(), 2);
                    modeloPrincipal.setValueAt(cpf, tbPrincipal.getSelectedRow(), 3);
                    modeloPrincipal.setValueAt(enderecoDTO.getLogradouro(), tbPrincipal.getSelectedRow(), 4);
                    modeloPrincipal.setValueAt(enderecoDTO.getCep(), tbPrincipal.getSelectedRow(), 5);
                    modeloPrincipal.setValueAt(dateFormat.format(dataNascimento), tbPrincipal.getSelectedRow(), 6);
                    MensagensUtil.msgInfo(this, (sexo == 'F' ? "A " : "O ") + nome + " foi atualizad" + (sexo == 'F' ? "a" : "o") + " com sucesso!", "Atualização bem sucedida");
                }

            }
            btLimparActionPerformed(null);
            btLimparEnderecoActionPerformed(null);
            enderecoDTO = null;
            pessoaDTO = null;
            idPessoa = null;
            btVoltarActionPerformed(null);
            //            this.dispose();
            //            main(null);
            //            pgPrincipal.setSelectedIndex(1);
        } catch (ValidacaoException ex) {
            MensagensUtil.msgErr(this, ex.getMessage(), "Campo(s) preechido(s) incorretamente");
        } catch (PersistenciaException | NegocioException ex) {
            MensagensUtil.msgErr(this, ex.getMessage(), "Erro!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btCadastrarAtualizarActionPerformed

    private void btLimparEnderecoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimparEnderecoActionPerformed
        cbEstado.setSelectedIndex(0);
        ctLogradouro.setText(null);
        ctBairro.setText(null);
        ctCep.setText(null);
        ctNumero.setText(null);

        numero = null;
        logradouro = null;
        bairro = null;
        cep = null;
        estadoDTO = null;

        lbLogradouro.setText(null);
        lbNumero.setText(null);
        lbCep.setText(null);
        lbBairro.setText(null);
    }//GEN-LAST:event_btLimparEnderecoActionPerformed

    private void tbPrincipalMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPrincipalMouseReleased
        ativaDesativaBotoes(tbPrincipal.getSelectedRowCount() > 0);
    }//GEN-LAST:event_tbPrincipalMouseReleased

    private void btLimparSelecaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimparSelecaoActionPerformed
        tbPrincipal.getSelectionModel().clearSelection();
        ativaDesativaBotoes(false);
    }//GEN-LAST:event_btLimparSelecaoActionPerformed

    private void tbExcluidosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbExcluidosMouseReleased
        ativaDesativaBotoesExcluidos(tbExcluidos.getSelectedRowCount() > 0);
    }//GEN-LAST:event_tbExcluidosMouseReleased

    //Tela de Cadastro
    private void ctNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctNomeKeyReleased
        campoValido(lbNome, ctNome.getText().trim().length() > 2);
    }//GEN-LAST:event_ctNomeKeyReleased

    private void ctCpfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctCpfKeyReleased
        try {
            campoValido(lbCpf, ctCpf.getText().trim().length() == 14 && new PessoaDAO().getByCpf(ctCpf.getText().trim()) == null);
        } catch (PersistenciaException e) {
            e.printStackTrace();
            MensagensUtil.msgErr(this, "Erro ao validar CPF!", "Não foi possível validar");
        }
    }//GEN-LAST:event_ctCpfKeyReleased

    private void ctDataNascimentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctDataNascimentoKeyReleased
        campoValido(lbDataNascimento, ctDataNascimento.getText().trim().length() == 10);
    }//GEN-LAST:event_ctDataNascimentoKeyReleased

    //Tela de Endereço
    private void ctLogradouroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctLogradouroKeyReleased
        campoValido(lbLogradouro, ctLogradouro.getText().trim().length() > 4);
    }//GEN-LAST:event_ctLogradouroKeyReleased

    private void ctNumeroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctNumeroKeyReleased
        campoValido(lbNumero, ctNumero.getText().length() > 0);
    }//GEN-LAST:event_ctNumeroKeyReleased

    private void ctCepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctCepKeyReleased
        campoValido(lbCep, ctCep.getText().trim().length() == 9);
    }//GEN-LAST:event_ctCepKeyReleased

    private void ctBairroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ctBairroKeyReleased
        campoValido(lbBairro, ctBairro.getText().trim().length() > 2);
    }//GEN-LAST:event_ctBairroKeyReleased

    private void pExcluidosComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pExcluidosComponentShown
        carregarTabelaExcluidos();
    }//GEN-LAST:event_pExcluidosComponentShown

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CrudFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrudFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrudFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrudFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrudFrame(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgOrdem;
    private javax.swing.ButtonGroup bgSexo;
    private javax.swing.ButtonGroup bgSexoConsulta;
    private javax.swing.JRadioButton brCpf;
    private javax.swing.JRadioButton brFeminino;
    private javax.swing.JRadioButton brFemininoConsulta;
    private javax.swing.JRadioButton brMasculino;
    private javax.swing.JRadioButton brMasculinoConsulta;
    private javax.swing.JRadioButton brNome;
    private javax.swing.JButton btCadastrarAtualizar;
    private javax.swing.JButton btConsultar;
    private javax.swing.JButton btDesativar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btLimpar;
    private javax.swing.JButton btLimparEndereco;
    private javax.swing.JButton btLimparSelecao;
    private javax.swing.JButton btNext;
    private javax.swing.JButton btRecarregar;
    private javax.swing.JButton btRecarregarExcluidos;
    private javax.swing.JButton btRestaurar;
    private javax.swing.JButton btVoltar;
    private javax.swing.JComboBox<String> cbEstado;
    private javax.swing.JComboBox<String> cbEstadoConsulta;
    private javax.swing.JTextField ctBairro;
    private javax.swing.JFormattedTextField ctCep;
    private javax.swing.JFormattedTextField ctCpf;
    private javax.swing.JTextField ctCpfConsulta;
    private javax.swing.JFormattedTextField ctDataNascimento;
    private javax.swing.JTextField ctLogradouro;
    private javax.swing.JTextField ctNome;
    private javax.swing.JTextField ctNomeConsulta;
    private javax.swing.JTextField ctNumero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbBairro;
    private javax.swing.JLabel lbCep;
    private javax.swing.JLabel lbCpf;
    private javax.swing.JLabel lbDataNascimento;
    private javax.swing.JLabel lbLogradouro;
    private javax.swing.JLabel lbNome;
    private javax.swing.JLabel lbNumero;
    private javax.swing.JPanel pCadastro;
    private javax.swing.JPanel pConsulta;
    private javax.swing.JPanel pEndereco;
    private javax.swing.JPanel pExcluidos;
    private javax.swing.JPanel pLista;
    private javax.swing.JTabbedPane pgPrincipal;
    private javax.swing.JScrollPane spLista;
    private javax.swing.JScrollPane spLista2;
    private javax.swing.JScrollPane spLista3;
    private javax.swing.JTable tbConsulta;
    private javax.swing.JTable tbExcluidos;
    private javax.swing.JTable tbPrincipal;
    // End of variables declaration//GEN-END:variables
}
