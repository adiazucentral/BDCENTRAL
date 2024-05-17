import Patient from "../../models/patient/patient";

export const savePatients = async (patients: PatientDB[]) => {
    try {
        const options = { upsert: true, new: true };
        let patientsInserted: any[] = [];
        for (const patient of patients) {
            patient.lab21c12 = new Date(patient.lab21c12);
            let filter = { lab21c1: patient.lab21c1, lab21c12: { $lte: patient.lab21c12 } };
            await Patient.findOneAndUpdate(filter, { $set: { ...patient } }, options)
                .then(doc => {
                    if (doc) {
                        patientsInserted.push({
                            lab21c1: doc.lab21c1,
                            created: doc.createdAt,
                            updated: doc.updatedAt
                        });
                    }
                }).catch(err => {
                    console.error('Error al insertar el paciente:', err);
                });
        }
        return patientsInserted;
    } catch (error) {
        console.log("Error al insertar pacientes: ", error);
    }
}

