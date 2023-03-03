select p.nome, p.cpf , p.email , p.datanascimento , p.sexo, p.mae 
, f.matricula , f.tipofuncionario_id , f.matricula 
from pessoa p
join funcionario f ON p.id = f.pessoafisica_id 
where f.tipofuncionario_id = 1
and f.matricula = '10233'