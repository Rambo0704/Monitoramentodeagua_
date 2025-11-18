const API_BASE = "http://localhost:8080";
const AUTH_KEY = "monitoramentoAuth";

let auth = { token: null, nome: null };
let modoEdicao = {
    ativo: false,
    id: null,
    modulo: null
};

document.addEventListener('DOMContentLoaded', init);

function init() {
    const dadosSalvos = localStorage.getItem(AUTH_KEY);
    if (dadosSalvos) {
        auth = JSON.parse(dadosSalvos);
    }
    
    atualizarInterface();
    
    if (auth.token) {
        showPanel('menu-card');
    } else {
        showPanel('login-card');
    }
    
    document.getElementById('btnLogin').addEventListener('click', login);
    document.getElementById('btnLogoutMenu').addEventListener('click', logout);

    document.querySelectorAll('.btn-menu').forEach(btn => {
        btn.addEventListener('click', () => {
            const panelId = btn.getAttribute('data-panel');
            showPanel(panelId);
            
            if (panelId === 'panel-leituras') listarLeituras();
            if (panelId === 'panel-usuarios') listarUsuarios();
            if (panelId === 'panel-imoveis') listarImoveis();
            if (panelId === 'panel-contratos') listarContratos();
        });
    });

    document.querySelectorAll('.btn-voltar').forEach(btn => {
        btn.addEventListener('click', () => showPanel('menu-card'));
    });

    setupLeituraListeners();
    setupUsuarioListeners();
    setupImovelListeners();
    setupContratoListeners();
}


function showPanel(panelId) {
    document.getElementById('login-card').style.display = 'none';
    document.getElementById('menu-card').style.display = 'none';
    
    document.querySelectorAll('.panel').forEach(p => {
        p.style.display = 'none';
    });

    if (panelId === 'login-card') {
        document.getElementById('login-card').style.display = 'block';
    } else {
        const panel = document.getElementById(panelId);
        if (panel) {
            panel.style.display = 'block';
        }
    }
    cancelarEdicaoGeral();
}

function atualizarInterface() {
    if (auth.token) {
        document.getElementById('spanNomeUsuario').textContent = auth.nome;
    } else {
        showPanel('login-card');
    }
}

async function apiFetch(endpoint, options = {}) {
    const statusElementId = (options.modulo ? options.modulo + '-' : '') + 'status';
    const status = document.getElementById(statusElementId);

    if (!auth.token && !options.isLogin) {
        if (status) status.textContent = 'Faça login primeiro.';
        return null;
    }

    try {
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };
        
        if (auth.token) {
            headers['Authorization'] = 'Bearer ' + auth.token;
        }

        const resp = await fetch(API_BASE + endpoint, {
            ...options,
            headers: headers
        });

        if (resp.status === 401 || resp.status === 403) {
             if (status) {
                 status.textContent = 'Sessão expirada. Faça login novamente.';
                 status.classList.add('text-red-600');
             }
             logout();
             return null;
        }
        
        if (!resp.ok) {
            let errorMsg = `Erro HTTP ${resp.status}`;
            try {
                const errData = await resp.json();
                errorMsg = errData.message || JSON.stringify(errData);
            } catch(e) {
                errorMsg = await resp.text() || errorMsg;
            }
             if (status) {
                status.textContent = `Erro: ${errorMsg}`;
                status.classList.add('text-red-600');
             }
            return null;
        }
        
        if (resp.status === 204) {
            return { ok: true, data: null };
        }

        const data = await resp.json();
        return { ok: true, data: data };

    } catch (e) {
        console.error("Erro na API:", e);
        if (status) {
            status.textContent = 'Erro ao conectar com o backend.';
            status.classList.add('text-red-600');
        }
        return null;
    }
}


async function login() {
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const status = document.getElementById('loginStatus');
    status.textContent = 'Autenticando...';

    const result = await apiFetch('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, senha }),
        isLogin: true
    });

    if (result && result.ok && result.data.token) {
        auth.token = result.data.token;
        auth.nome = result.data.nomeUsuario;
        localStorage.setItem(AUTH_KEY, JSON.stringify(auth));
        
        status.textContent = 'Login bem-sucedido!';
        status.classList.add('text-green-600');
        
        atualizarInterface();
        showPanel('menu-card');
    } else {
        status.textContent = 'Login inválido';
        status.classList.add('text-red-600');
    }
}

function logout() {
    auth = { token: null, nome: null };
    localStorage.removeItem(AUTH_KEY);
    atualizarInterface();
}

function setStatus(modulo, mensagem, tipo = 'info') {
    const status = document.getElementById(`${modulo}-status`);
    if (!status) return;
    status.textContent = mensagem;
    status.classList.remove('text-red-600', 'text-green-600', 'text-gray-600');
    if (tipo === 'erro') {
        status.classList.add('text-red-600');
    } else if (tipo === 'sucesso') {
        status.classList.add('text-green-600');
    } else {
        status.classList.add('text-gray-600');
    }
}

function cancelarEdicaoGeral() {
    if (!modoEdicao.ativo) return;
    
    const modulo = modoEdicao.modulo;
    
    document.getElementById(`${modulo}-btnSalvar`).textContent = 'Criar';
    document.getElementById(`${modulo}-btnSalvar`).classList.remove('bg-blue-600', 'hover:bg-blue-700');
    document.getElementById(`${modulo}-btnSalvar`).classList.add('bg-green-600', 'hover:bg-green-700');
    document.getElementById(`${modulo}-btnCancelar`).style.display = 'none';

    if (modulo === 'leitura') {
        document.getElementById('leitura-numSerie').disabled = false;
        limparFormLeitura();
    } else if (modulo === 'usuario') {
        document.getElementById('usuario-tipoP').disabled = false;
        limparFormUsuario();
    } else if (modulo === 'imovel') {
        limparFormImovel();
    } else if (modulo === 'contrato') {
        limparFormContrato();
    }

    modoEdicao.ativo = false;
    modoEdicao.id = null;
    modoEdicao.modulo = null;
}

function prepararEdicaoGeral(modulo, id, data) {
    modoEdicao.ativo = true;
    modoEdicao.id = id;
    modoEdicao.modulo = modulo;

    document.getElementById(`${modulo}-btnSalvar`).textContent = 'Salvar Alterações';
    document.getElementById(`${modulo}-btnSalvar`).classList.add('bg-blue-600', 'hover:bg-blue-700');
    document.getElementById(`${modulo}-btnSalvar`).classList.remove('bg-green-600', 'hover:bg-green-700');
    document.getElementById(`${modulo}-btnCancelar`).style.display = 'block';

    if (modulo === 'leitura') {
        document.getElementById('leitura-numSerie').value = data.numSerie;
        document.getElementById('leitura-valorMedido').value = data.valorMedido;
        document.getElementById('leitura-numSerie').disabled = true;
    } else if (modulo === 'usuario') {
        document.getElementById('usuario-nome').value = data.nome;
        document.getElementById('usuario-email').value = data.email;
        document.getElementById('usuario-senha').value = '';
        document.getElementById('usuario-tipoP').value = data.tipoP;
        document.getElementById('usuario-tipoP').disabled = true;
        toggleUsuarioCampos();
        document.getElementById('usuario-cpf').value = data.cpf || '';
        document.getElementById('usuario-dataNasc').value = data.dataNasc || '';
        document.getElementById('usuario-cnpj').value = data.cnpj || '';
        document.getElementById('usuario-razaoSocial').value = data.razaoSocial || '';
    } else if (modulo === 'imovel') {
        document.getElementById('imovel-tipo').value = data.tipoImovel;
        document.getElementById('imovel-cep').value = data.cep;
        document.getElementById('imovel-rua').value = data.rua;
        document.getElementById('imovel-bairro').value = data.bairro;
        document.getElementById('imovel-cidade').value = data.cidade;
        document.getElementById('imovel-estado').value = data.estado;
    } else if (modulo === 'contrato') {
        document.getElementById('contrato-codUsuario').value = data.codUsuario;
        document.getElementById('contrato-codImovel').value = data.codImovel;
        document.getElementById('contrato-numSerieHidrometro').value = data.numSerieHidrometro;
        document.getElementById('contrato-status').value = data.status;
    }

    window.scrollTo(0, 0);
}


function setupLeituraListeners() {
    document.getElementById('leitura-btnListar').addEventListener('click', listarLeituras);
    document.getElementById('leitura-btnSalvar').addEventListener('click', salvarLeitura);
    document.getElementById('leitura-btnCancelar').addEventListener('click', cancelarEdicaoGeral);
}

function limparFormLeitura() {
    document.getElementById('leitura-numSerie').value = '';
    document.getElementById('leitura-valorMedido').value = '';
}

async function listarLeituras() {
    setStatus('leitura', 'Carregando leituras...');
    const result = await apiFetch('/api/leituras', { modulo: 'leitura' });
    if (!result || !result.ok) return;

    const leituras = result.data;
    const tbody = document.getElementById('leitura-tabela');
    tbody.innerHTML = ''; 

    if (leituras.length === 0) {
        setStatus('leitura', 'Nenhuma leitura encontrada.');
        return;
    }

    leituras.forEach(l => {
        const tr = document.createElement('tr');
        tr.className = 'hover:bg-gray-50';
        
        const numSerie = l.hidrometro ? l.hidrometro.numSerieHidrometro : 'N/D';
        const dataFormatada = new Date(l.dataHoraLeitura).toLocaleString('pt-BR');
        const valorFormatado = l.valorMedido.toFixed(3);

        tr.innerHTML = `
            <td class="px-6 py-4 text-sm">${l.codLeitura}</td>
            <td class="px-6 py-4 text-sm">${numSerie}</td>
            <td class="px-6 py-4 text-sm">${valorFormatado}</td>
            <td class="px-6 py-4 text-sm">${dataFormatada}</td>
            <td class="px-6 py-4 text-sm">
                <button onclick="prepararEdicaoLeitura('${l.codLeitura}', '${numSerie}', ${l.valorMedido})" class="text-blue-600">Editar</button>
                <button onclick="deletarLeitura('${l.codLeitura}')" class="text-red-600 ml-3">Excluir</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
    setStatus('leitura', 'Total de leituras: ' + leituras.length, 'info');
}

function prepararEdicaoLeitura(id, numSerie, valorMedido) {
    prepararEdicaoGeral('leitura', id, { numSerie, valorMedido });
}

async function salvarLeitura() {
    const valorMedido = parseFloat(document.getElementById('leitura-valorMedido').value);
    if (isNaN(valorMedido)) {
        setStatus('leitura', 'O Valor Medido é inválido.', 'erro');
        return;
    }

    let url = '/api/leituras';
    let method = 'POST';
    let body = {};

    if (modoEdicao.ativo) {
        url = `/api/leituras/${modoEdicao.id}`;
        method = 'PUT';
        body = { valorMedido: valorMedido };
        setStatus('leitura', `Salvando alterações...`);
    } else {
        const numSerie = document.getElementById('leitura-numSerie').value;
        if (!numSerie) {
            setStatus('leitura', 'Preencha o Número de Série.', 'erro');
            return;
        }
        body = { numSerieHidrometro: numSerie, valorMedido: valorMedido };
        setStatus('leitura', 'Enviando leitura...');
    }

    const result = await apiFetch(url, { method: method, body: JSON.stringify(body), modulo: 'leitura' });
    if (result && result.ok) {
        setStatus('leitura', 'Leitura salva com sucesso.', 'sucesso');
        limparFormLeitura();
        cancelarEdicaoGeral();
        listarLeituras(); 
    }
}

async function deletarLeitura(id) {
    if (!confirm(`Tem certeza que deseja excluir a leitura ID ${id}?`)) return;
    
    setStatus('leitura', `Excluindo leitura ${id}...`);
    const result = await apiFetch(`/api/leituras/${id}`, { method: 'DELETE', modulo: 'leitura' });
    
    if (result && result.ok) {
        setStatus('leitura', `Leitura ${id} excluída com sucesso.`, 'sucesso');
        listarLeituras();
    }
}


function setupUsuarioListeners() {
    document.getElementById('usuario-btnListar').addEventListener('click', listarUsuarios);
    document.getElementById('usuario-btnSalvar').addEventListener('click', salvarUsuario);
    document.getElementById('usuario-btnCancelar').addEventListener('click', cancelarEdicaoGeral);
    document.getElementById('usuario-tipoP').addEventListener('change', toggleUsuarioCampos);
}

function toggleUsuarioCampos() {
    const tipo = document.getElementById('usuario-tipoP').value;
    if (tipo === 'FISICA') {
        document.getElementById('usuario-campos-pf').style.display = 'block';
        document.getElementById('usuario-campos-pj').style.display = 'none';
    } else {
        document.getElementById('usuario-campos-pf').style.display = 'none';
        document.getElementById('usuario-campos-pj').style.display = 'block';
    }
}

function limparFormUsuario() {
    document.getElementById('usuario-nome').value = '';
    document.getElementById('usuario-email').value = '';
    document.getElementById('usuario-senha').value = '';
    document.getElementById('usuario-tipoP').value = 'FISICA';
    document.getElementById('usuario-cpf').value = '';
    document.getElementById('usuario-dataNasc').value = '';
    document.getElementById('usuario-cnpj').value = '';
    document.getElementById('usuario-razaoSocial').value = '';
    toggleUsuarioCampos();
}

async function listarUsuarios() {
    setStatus('usuario', 'Carregando usuários...');
    const result = await apiFetch('/api/usuarios', { modulo: 'usuario' });
    if (!result || !result.ok) return;

    const usuarios = result.data;
    const tbody = document.getElementById('usuario-tabela');
    tbody.innerHTML = ''; 

    if (usuarios.length === 0) {
        setStatus('usuario', 'Nenhum usuário encontrado.');
        return;
    }

    usuarios.forEach(u => {
        const tr = document.createElement('tr');
        tr.className = 'hover:bg-gray-50';
        const documento = u.tipoP === 'FISICA' ? u.cpf : u.cnpj;
        const dataNasc = u.dataNasc ? u.dataNasc : '';
        
        tr.innerHTML = `
            <td class="px-6 py-4 text-sm">${u.codUsuario}</td>
            <td class="px-6 py-4 text-sm">${u.nome}</td>
            <td class="px-6 py-4 text-sm">${u.email}</td>
            <td class="px-6 py-4 text-sm">${u.tipoP}</td>
            <td class="px-6 py-4 text-sm">${documento || 'N/D'}</td>
            <td class="px-6 py-4 text-sm">
                <button onclick='prepararEdicaoUsuario(${JSON.stringify(u)})' class="text-blue-600">Editar</button>
                <button onclick="deletarUsuario('${u.codUsuario}')" class="text-red-600 ml-3">Excluir</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
    setStatus('usuario', 'Total de usuários: ' + usuarios.length, 'info');
}

function prepararEdicaoUsuario(usuario) {
    prepararEdicaoGeral('usuario', usuario.codUsuario, usuario);
}

async function salvarUsuario() {
    const dto = {
        nome: document.getElementById('usuario-nome').value,
        email: document.getElementById('usuario-email').value,
        senha: document.getElementById('usuario-senha').value,
        tipoP: document.getElementById('usuario-tipoP').value,
        cpf: document.getElementById('usuario-cpf').value,
        dataNasc: document.getElementById('usuario-dataNasc').value || null,
        cnpj: document.getElementById('usuario-cnpj').value,
        razaoSocial: document.getElementById('usuario-razaoSocial').value
    };

    let url = '/api/usuarios';
    let method = 'POST';

    if (modoEdicao.ativo) {
        url = `/api/usuarios/${modoEdicao.id}`;
        method = 'PUT';
        if (!dto.senha) {
            delete dto.senha; 
        }
        setStatus('usuario', `Salvando alterações...`);
    } else {
        setStatus('usuario', 'Criando usuário...');
    }
    
    if (dto.dataNasc === "") dto.dataNasc = null;

    const result = await apiFetch(url, { method: method, body: JSON.stringify(dto), modulo: 'usuario' });
    if (result && result.ok) {
        setStatus('usuario', 'Usuário salvo com sucesso.', 'sucesso');
        limparFormUsuario();
        cancelarEdicaoGeral();
        listarUsuarios();
    }
}

async function deletarUsuario(id) {
    if (!confirm(`Tem certeza que deseja excluir o usuário ID ${id}?`)) return;
    
    setStatus('usuario', `Excluindo usuário ${id}...`);
    const result = await apiFetch(`/api/usuarios/${id}`, { method: 'DELETE', modulo: 'usuario' });
    
    if (result && result.ok) {
        setStatus('usuario', `Usuário ${id} excluído com sucesso.`, 'sucesso');
        listarUsuarios();
    }
}


function setupImovelListeners() {
    document.getElementById('imovel-btnListar').addEventListener('click', listarImoveis);
    document.getElementById('imovel-btnSalvar').addEventListener('click', salvarImovel);
    document.getElementById('imovel-btnCancelar').addEventListener('click', cancelarEdicaoGeral);
}

function limparFormImovel() {
    document.getElementById('imovel-tipo').value = '';
    document.getElementById('imovel-cep').value = '';
    document.getElementById('imovel-rua').value = '';
    document.getElementById('imovel-bairro').value = '';
    document.getElementById('imovel-cidade').value = '';
    document.getElementById('imovel-estado').value = '';
}

async function listarImoveis() {
    setStatus('imovel', 'Carregando imóveis...');
    const result = await apiFetch('/api/imoveis', { modulo: 'imovel' });
    if (!result || !result.ok) return;

    const imoveis = result.data;
    const tbody = document.getElementById('imovel-tabela');
    tbody.innerHTML = ''; 

    if (imoveis.length === 0) {
        setStatus('imovel', 'Nenhum imóvel encontrado.');
        return;
    }

    imoveis.forEach(i => {
        const tr = document.createElement('tr');
        tr.className = 'hover:bg-gray-50';
        const endereco = `${i.rua || 'N/D'}, ${i.bairro || 'N/D'} - ${i.cidade || 'N/D'}`;
        
        tr.innerHTML = `
            <td class="px-6 py-4 text-sm">${i.codImovel}</td>
            <td class="px-6 py-4 text-sm">${i.tipoImovel}</td>
            <td class="px-6 py-4 text-sm">${i.cep}</td>
            <td class="px-6 py-4 text-sm">${endereco}</td>
            <td class="px-6 py-4 text-sm">
                <button onclick='prepararEdicaoImovel(${JSON.stringify(i)})' class="text-blue-600">Editar</button>
                <button onclick="deletarImovel('${i.codImovel}')" class="text-red-600 ml-3">Excluir</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
    setStatus('imovel', 'Total de imóveis: ' + imoveis.length, 'info');
}

function prepararEdicaoImovel(imovel) {
    prepararEdicaoGeral('imovel', imovel.codImovel, imovel);
}

async function salvarImovel() {
    const dto = {
        tipoImovel: document.getElementById('imovel-tipo').value,
        cep: document.getElementById('imovel-cep').value,
        rua: document.getElementById('imovel-rua').value,
        bairro: document.getElementById('imovel-bairro').value,
        cidade: document.getElementById('imovel-cidade').value,
        estado: document.getElementById('imovel-estado').value
    };

    let url = '/api/imoveis';
    let method = 'POST';

    if (modoEdicao.ativo) {
        url = `/api/imoveis/${modoEdicao.id}`;
        method = 'PUT';
        setStatus('imovel', `Salvando alterações...`);
    } else {
        setStatus('imovel', 'Criando imóvel...');
    }

    const result = await apiFetch(url, { method: method, body: JSON.stringify(dto), modulo: 'imovel' });
    if (result && result.ok) {
        setStatus('imovel', 'Imóvel salvo com sucesso.', 'sucesso');
        limparFormImovel();
        cancelarEdicaoGeral();
        listarImoveis();
    }
}

async function deletarImovel(id) {
    if (!confirm(`Tem certeza que deseja excluir o imóvel ID ${id}?`)) return;
    
    setStatus('imovel', `Excluindo imóvel ${id}...`);
    const result = await apiFetch(`/api/imoveis/${id}`, { method: 'DELETE', modulo: 'imovel' });
    
    if (result && result.ok) {
        setStatus('imovel', `Imóvel ${id} excluído com sucesso.`, 'sucesso');
        listarImoveis();
    }
}


function setupContratoListeners() {
    document.getElementById('contrato-btnListar').addEventListener('click', listarContratos);
    document.getElementById('contrato-btnSalvar').addEventListener('click', salvarContrato);
    document.getElementById('contrato-btnCancelar').addEventListener('click', cancelarEdicaoGeral);
}

function limparFormContrato() {
    document.getElementById('contrato-codUsuario').value = '';
    document.getElementById('contrato-codImovel').value = '';
    document.getElementById('contrato-numSerieHidrometro').value = '';
    document.getElementById('contrato-status').value = '';
}

async function listarContratos() {
    setStatus('contrato', 'Carregando contratos...');
    const result = await apiFetch('/api/contratos', { modulo: 'contrato' });
    if (!result || !result.ok) return;

    const contratos = result.data;
    const tbody = document.getElementById('contrato-tabela');
    tbody.innerHTML = ''; 

    if (contratos.length === 0) {
        setStatus('contrato', 'Nenhum contrato encontrado.');
        return;
    }

    contratos.forEach(c => {
        const tr = document.createElement('tr');
        tr.className = 'hover:bg-gray-50';
        
        tr.innerHTML = `
            <td class="px-6 py-4 text-sm">${c.codContrato}</td>
            <td class="px-6 py-4 text-sm">${c.nomeUsuario} (${c.codUsuario})</td>
            <td class="px-6 py-4 text-sm">${c.cepImovel} (${c.codImovel})</td>
            <td class="px-6 py-4 text-sm">${c.numSerieHidrometro}</td>
            <td class="px-6 py-4 text-sm">${c.status}</td>
            <td class="px-6 py-4 text-sm">
                <button onclick='prepararEdicaoContrato(${JSON.stringify(c)})' class="text-blue-600">Editar</button>
                <button onclick="deletarContrato('${c.codContrato}')" class="text-red-600 ml-3">Excluir</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
    setStatus('contrato', 'Total de contratos: ' + contratos.length, 'info');
}

function prepararEdicaoContrato(contrato) {
    prepararEdicaoGeral('contrato', contrato.codContrato, contrato);
}

async function salvarContrato() {
    const dto = {
        codUsuario: document.getElementById('contrato-codUsuario').value,
        codImovel: document.getElementById('contrato-codImovel').value,
        numSerieHidrometro: document.getElementById('contrato-numSerieHidrometro').value,
        status: document.getElementById('contrato-status').value
    };

    let url = '/api/contratos';
    let method = 'POST';

    if (modoEdicao.ativo) {
        url = `/api/contratos/${modoEdicao.id}`;
        method = 'PUT';
        setStatus('contrato', `Salvando alterações...`);
    } else {
        setStatus('contrato', 'Criando contrato...');
    }

    const result = await apiFetch(url, { method: method, body: JSON.stringify(dto), modulo: 'contrato' });
    if (result && result.ok) {
        setStatus('contrato', 'Contrato salvo com sucesso.', 'sucesso');
        limparFormContrato();
        cancelarEdicaoGeral();
        listarContratos();
    }
}

async function deletarContrato(id) {
    if (!confirm(`Tem certeza que deseja excluir o contrato ID ${id}?`)) return;
    
    setStatus('contrato', `Excluindo contrato ${id}...`);
    const result = await apiFetch(`/api/contratos/${id}`, { method: 'DELETE', modulo: 'contrato' });
    
    if (result && result.ok) {
        setStatus('contrato', `Contrato ${id} excluído com sucesso.`, 'sucesso');
        listarContratos();
    }
}