import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Notas ALP | Calculadora acadêmica",
  description: "Calcule sua média, entenda o próximo passo e acompanhe sua jornada acadêmica.",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="pt-BR"><body>{children}</body></html>;
}
