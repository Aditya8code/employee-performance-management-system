package com.eps.util;

import com.eps.model.Evaluation;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

/**
 * Utility to generate RFC 4180 compliant CSV export for performance appraisal reports.
 */
public final class CSVExporter {

    private CSVExporter() {
    }

    /**
     * Exports evaluation records as a CSV stream.
     */
    public static void exportEvaluationsToCSV(List<Evaluation> evaluations, Writer writer) {
        PrintWriter out = (writer instanceof PrintWriter) ? (PrintWriter) writer : new PrintWriter(writer);

        // Header Row
        out.println("Evaluation ID,Employee Name,Email,Department,Job Title,Manager,Evaluation Cycle,Total Score,Rating,Status,Submitted At");

        if (evaluations != null) {
            for (Evaluation eval : evaluations) {
                out.print(escape(eval.getId()));
                out.print(",");
                out.print(escape(eval.getEmployeeName()));
                out.print(",");
                out.print(escape(eval.getEmployeeEmail()));
                out.print(",");
                out.print(escape(eval.getDepartmentName()));
                out.print(",");
                out.print(escape(eval.getEmployeeTitle()));
                out.print(",");
                out.print(escape(eval.getManagerName()));
                out.print(",");
                out.print(escape(eval.getCycleName()));
                out.print(",");
                out.print(escape(eval.getTotalScore() != null ? eval.getTotalScore().toString() : "N/A"));
                out.print(",");
                out.print(escape(eval.getRatingLabel() != null ? eval.getRatingLabel() : "Pending"));
                out.print(",");
                out.print(escape(eval.getStatus()));
                out.print(",");
                out.print(escape(eval.getSubmittedAt() != null ? eval.getSubmittedAt().toString() : "N/A"));
                out.println();
            }
        }
        out.flush();
    }

    private static String escape(Object obj) {
        if (obj == null) {
            return "\"\"";
        }
        String str = obj.toString();
        // Escape quotes
        str = str.replace("\"", "\"\"");
        return "\"" + str + "\"";
    }
}
