"use client";

import { useMemo, useState } from "react";
import { ArrowDownRight, Check, CircleHelp, RotateCcw } from "lucide-react";
import { calculateGrade, type Course, type GradeResult } from "@/lib/grades";

const sections = ["Visão geral", "Calculadora", "Sua jornada"];

function NumberField({ label, value, onChange, optional = false }: { label: string; value: string; onChange: (value: string) => void; optional?: boolean }) {
  return <div className="field"><label>{label} {optional && "· opcional"}</label><input inputMode="decimal" type="number" min="0" max="10" step="0.1" value={value} onChange={(event) => onChange(event.target.value)} placeholder="0,0" /></div>;
}

function ResultCard({ result }: { result: GradeResult }) {
  const badgeClass = result.status === "approved" ? "result-badge" : result.status === "failed" ? "result-badge warning" : "result-badge neutral";
  return <aside className="panel result-panel"><span className="eyebrow">Leitura do resultado</span><div className="result-title">{result.finalAverage.toFixed(2).replace(".", ",")}</div><p className="result-copy">{result.message}</p>{result.nextStep && <p className="result-copy" style={{ marginTop: 12 }}>{result.nextStep}</p>}<span className={badgeClass}>{result.label.toUpperCase()}</span></aside>;
}

export default function Home() {
  const [course, setCourse] = useState<Course>("ALP");
  const [p1, setP1] = useState("");
  const [lists, setLists] = useState("");
  const [p2, setP2] = useState("");
  const [p3, setP3] = useState("");
  const [exam, setExam] = useState("");
  const [calculated, setCalculated] = useState(false);
  const [activeSection, setActiveSection] = useState("Visão geral");

  const result = useMemo(() => calculateGrade({ course, p1: Number(p1) || 0, lists: Number(lists) || 0, p2: Number(p2) || 0, p3: p3 ? Number(p3) : undefined, exam: exam ? Number(exam) : undefined }), [course, p1, lists, p2, p3, exam]);
  const reset = () => { setP1(""); setLists(""); setP2(""); setP3(""); setExam(""); setCalculated(false); };
  const goTo = (id: string) => { setActiveSection(id); document.getElementById(id.toLowerCase().replaceAll(" ", "-"))?.scrollIntoView({ behavior: "smooth" }); };
  const needsP3 = calculated && course === "ALP" && result.status !== "approved" && result.status !== "failed" && !p3;
  const needsExam = calculated && result.status === "exam" && !exam;

  return <div className="app-shell">
    <aside className="sidebar"><a className="brand" href="#visão-geral"><span className="brand-mark" /><span><strong>Notas ALP</strong><small>clareza para decidir</small></span></a><div className="side-title">Índice de uso</div><nav className="nav">{sections.map((section, index) => <button key={section} className={activeSection === section ? "active" : ""} onClick={() => goTo(section)}><span>0{index + 1}</span>{section}</button>)}</nav><div className="side-note"><strong>Uma média por vez.</strong>Preencha suas notas e entenda o caminho acadêmico sem cálculos escondidos.</div></aside>
    <main className="main"><header className="topbar"><span>Notas ALP <span style={{ color: "var(--line)" }}>›</span> painel de estudo</span><span className="topbar-right"><span><i className="status-dot" /> cálculo local seguro</span><span>v. 01 / 2026</span></span></header>
      <div className="content">
        <section className="hero" id="visão-geral"><div><span className="eyebrow">01 · ponto de partida</span><h1>Suas notas não são um <em>mistério.</em></h1><p className="lede">Uma calculadora acadêmica que mostra a média, explica a regra e aponta o próximo passo — com transparência em cada etapa.</p><div className="hero-actions"><button className="button" onClick={() => goTo("Calculadora")}>Calcular minha média <ArrowDownRight size={15} /></button><button className="button secondary" onClick={() => goTo("Sua jornada")}>Como funciona?</button></div></div><div className="hero-art"><div className="art-card"><strong>Entenda o caminho.</strong><p>Da média inicial ao exame final, cada decisão aparece no momento certo.</p></div></div></section>
        <div className="stats"><div className="stat"><strong>02</strong><span>disciplinas mapeadas</span></div><div className="stat"><strong>06,0</strong><span>média de aprovação</span></div><div className="stat"><strong>100%</strong><span>regra explicada</span></div></div>
        <section className="section" id="calculadora"><div className="section-heading"><div><span className="eyebrow">02 · cálculo aberto</span><h2>Faça a conta, <em>sem ruído.</em></h2></div><p>Os dados ficam no seu navegador nesta primeira versão. O serviço está preparado para conectar uma API quando o back-end evoluir.</p></div><div className="calculator"><div className="panel"><div className="panel-heading"><div><span className="eyebrow">Dados da disciplina</span><h3>Notas do semestre</h3><p>Use valores entre 0 e 10.</p></div><select className="select" value={course} onChange={(event) => { setCourse(event.target.value as Course); setCalculated(false); }}><option value="ALP">ALP · com listas</option><option value="OS">OS · duas provas</option></select></div><div className="fields"><NumberField label="Prova 1 · P1" value={p1} onChange={setP1} /><NumberField label="Listas" value={lists} onChange={setLists} optional={course === "OS"} /><NumberField label="Prova 2 · P2" value={p2} onChange={setP2} /></div>{course === "ALP" && (needsP3 || p3) && <div className="fields"><NumberField label="Prova 3 · P3" value={p3} onChange={setP3} optional={!needsP3} />{(needsExam || exam) && <NumberField label="Exame final" value={exam} onChange={setExam} />}</div>}<p className="helper"><CircleHelp size={14} style={{ verticalAlign: "-3px", marginRight: 5 }} /> A P3 e o exame aparecem quando a sua média indicar essa necessidade.</p><button className="button calculate" onClick={() => setCalculated(true)}>Ver minha situação <Check size={15} /></button></div>{calculated ? <ResultCard result={result} /> : <aside className="panel result-panel"><span className="eyebrow">Leitura do resultado</span><div className="result-title">—</div><p className="result-copy">Preencha suas notas e descubra o próximo passo.</p><span className="result-badge neutral">AGUARDANDO DADOS</span></aside>}</div></section>
        <section className="section" id="sua-jornada"><div className="section-heading"><div><span className="eyebrow">03 · sua jornada</span><h2>O que acontece <em>depois?</em></h2></div><button className="button secondary" onClick={reset}><RotateCcw size={14} /> Limpar cálculo</button></div><div className="timeline"><div className="step current"><span className="step-number">01 / média inicial</span><h3>Comece pelo básico.</h3><p>Insira P1, listas e P2 para saber se a aprovação já está garantida.</p></div><div className="step"><span className="step-number">02 / recuperação</span><h3>P3, se precisar.</h3><p>Abaixo de 6,0, a P3 entra no cálculo e abre uma nova chance.</p></div><div className="step"><span className="step-number">03 / decisão</span><h3>Exame final.</h3><p>Entre 4,0 e 6,0 após a P3, você pode informar a nota do exame.</p></div></div></section>
        <footer className="footer"><span>Notas ALP · ferramenta de estudo</span><span>feito para tornar a regra legível</span></footer>
      </div></main>
  </div>;
}
