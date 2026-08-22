import type { GradeInput, GradeResult } from "./grades";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function calculateFromApi(input: GradeInput): Promise<GradeResult> {
  if (!API_URL) throw new Error("NEXT_PUBLIC_API_URL não configurada");
  const response = await fetch(`${API_URL}/api/grades/calculate`, { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(input) });
  if (!response.ok) throw new Error("Não foi possível calcular a média no servidor");
  return response.json() as Promise<GradeResult>;
}
