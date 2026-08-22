export type Course = "ALP" | "OS";
export type GradeStatus = "approved" | "p3" | "exam" | "failed";

export type GradeInput = { course: Course; p1: number; lists?: number; p2: number; p3?: number; exam?: number };
export type GradeResult = { initialAverage: number; finalAverage: number; status: GradeStatus; label: string; message: string; nextStep?: string };

const round = (value: number) => Math.round(value * 100) / 100;

export function calculateGrade(input: GradeInput): GradeResult {
  const { course, p1, p2, p3, exam } = input;
  const initialAverage = course === "ALP" ? 0.35 * p1 + 0.15 * (input.lists ?? 0) + 0.5 * p2 : (p1 + p2) / 2;
  if (course === "OS") {
    const approved = initialAverage >= 6;
    return { initialAverage: round(initialAverage), finalAverage: round(initialAverage), status: approved ? "approved" : "failed", label: approved ? "Aprovado" : "Reprovado", message: approved ? "Sua média final atende ao critério de aprovação." : "Sua média ficou abaixo de 6,0.", nextStep: approved ? undefined : "Revise o regulamento de recuperação da disciplina." };
  }
  if (initialAverage >= 6) return { initialAverage: round(initialAverage), finalAverage: round(initialAverage), status: "approved", label: "Aprovado", message: "Você alcançou a média necessária sem recuperação." };
  if (p3 === undefined) return { initialAverage: round(initialAverage), finalAverage: round(initialAverage), status: "p3", label: "P3 necessária", message: "A média inicial ficou abaixo de 6,0. Informe a P3 para continuar.", nextStep: "Adicione a nota da P3." };
  const finalAverage = 0.35 * Math.max(p1, p3) + 0.15 * (input.lists ?? 0) + 0.5 * p2;
  if (finalAverage >= 6) return { initialAverage: round(initialAverage), finalAverage: round(finalAverage), status: "approved", label: "Aprovado", message: "A P3 elevou sua média para a faixa de aprovação." };
  if (finalAverage < 4) return { initialAverage: round(initialAverage), finalAverage: round(finalAverage), status: "failed", label: "Reprovado", message: "A média ficou abaixo de 4,0 e não permite exame final." };
  if (exam === undefined) return { initialAverage: round(initialAverage), finalAverage: round(finalAverage), status: "exam", label: "Exame final", message: "Você está elegível para o exame final.", nextStep: "Adicione a nota do exame final." };
  const passedExam = exam >= 6;
  return { initialAverage: round(initialAverage), finalAverage: round(exam), status: passedExam ? "approved" : "failed", label: passedExam ? "Aprovado via exame" : "Reprovado", message: passedExam ? "O exame final confirmou sua aprovação." : "A nota do exame final ficou abaixo de 6,0." };
}
