import { model, Schema, Model } from 'mongoose';

const PatientSchema = new Schema<PatientDB>({
    lab21c1: {
        type:  Number,
        unique : true,
        required: [true, "El id es requerido"],
        alias: 'idBd'
    },
    lab21c2: {
        type:  String,
        required: [true, "La historia es requerida"],
        alias: 'id'
    },
    lab21c3: {
        type:  String,
        required: [true, "El primer nombre es requerido"],
        alias: 'name_1'
    },
    lab21c4: {
        type:  String,
        alias: 'name_2'
    },
    lab21c5: {
        type:  String,
        required: [true, "El primer apellido es requerido"],
        alias: 'lastname'
    },
    lab21c6: {
        type:  String,
        alias: 'surname'
    },
    lab21c7: {
        type:  Date,
        alias: 'birthdate'
    },
    lab21c8: {
        type:  String,
        alias: 'email'
    },
    lab21c9: {
        type:  Number,
        alias: 'size'
    },
    lab21c10: {
        type:  Number,
        alias: 'weight'
    },
    lab21c11: {
        type:  Date,
        alias: 'dateOfDeath'
    },
    lab21c12: {
        type:  Date,
        required: [true, "La fecha de ultima modificación es requerida"],
        alias: 'updatedDate'
    },
    lab21c13: {
        type:  String,
        alias: 'diagnosis'
    },
    lab21c14: {
        type:  String,
        alias: 'photo'
    },
    lab21c15: {
        type:  Number,
        alias: 'block'
    },
    lab21c16: {
        type:  String,
        alias: 'phone'
    },
    lab21c17: {
        type:  String,
        alias: 'address'
    },
    lab21c18: {
        type:  String,
        alias: 'userWebQuery'
    },
    lab21c19: {
        type:  String,
        alias: 'passwordWebQuery'
    },
    lab21c20: {
        type:  Date,
        alias: 'dateCreation'
    },
    lab21c21: {
        type:  Number,
        alias: 'loggedWebQuery'
    },
    lab21c22: {
        type:  Number,
        alias: 'status'
    },
    lab21c23: {
        type:  Date,
        alias: 'datePatientStatusUpdate'
    },
    lab21c24: {
        type:  String,
        alias: 'whatsapp'
    },
    lab80: {
        type: {
            lab80c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab80c2: {
                type: Number,
                alias: 'idFather'
            },
            lab80c3: {
                type: String,
                alias: 'code'
            },
            lab80c4: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'es-co'
            },
            lab80c5: {
                type: String,
                required: [true, "El nombre en ingles es requerido"],
                alias: 'en-us'
            },
        },
        alias: 'sex',
        _id: false
    },
    lab04c1: {
        type:  {
            lab04c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab04c2: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab04c3: {
                type: String,
                required: [true, "El apellido es requerido"],
                alias: 'lastname'
            },
            lab04c4: {
                type: String,
                required: [true, "El nombre de usuario es requerido"],
                alias: 'username'
            }
        },
        required: [true, "El usuario de trazabilidad es requerido"],
        alias: 'userCreated',
        _id: false
    },
    lab08: {
        type:  {
            lab08c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab08c2: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab08c5: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            }
        },
        alias: 'race',
        _id: false
    }, 
    lab54: {
        type:  {
            lab54c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab54c2: {
                type: String,
                required: [true, "La abreviatura es requerida"],
                alias: 'abbreviature'
            },
            lab54c3: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            }
        },
        alias: 'documentType',
        _id: false
    }, 
    lab04c1_2: {
        type:   {
            lab04c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab04c2: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab04c3: {
                type: String,
                required: [true, "El apellido es requerido"],
                alias: 'lastname'
            },
            lab04c4: {
                type: String,
                required: [true, "El nombre de usuario es requerido"],
                alias: 'username'
            }
        },
        alias: 'userCreation',
        _id: false
    },
    lab62: {
        type:  [
            {
                lab62c1: {
                    type: Number,
                    required: [true, "El id es requerido"],
                    alias: 'id'
                },
                lab62c2: {
                    type: String,
                    required: [true, "El nombre es requerido"],
                    alias: 'name'
                },
                lab62c3: {
                    type: String,
                    required: [true, "El origen es requerido"],
                    alias: 'origin'
                },
                lab62c4: {
                    type: Number,
                    required: [true, "El tipo de demográfico es requerido"],
                    alias: 'type'
                },
                lab62c5: {
                    type: Number,
                    required: [true, "El campo obligatorio es requerido"],
                    alias: 'required'
                },
                lab62c6: {
                    type: Number,
                    required: [true, "El orden es requerido"],
                    alias: 'order'
                },
                value: {
                    type: String
                },
                lab63: {
                    type: {
                        lab63c1: {
                            type: Number,
                            required: [true, "El id es requerido"],
                            alias: 'id'
                        },
                        lab63c2: {
                            type: String,
                            required: [true, "El código es requerido"],
                            alias: 'code'
                        },
                        lab63c3: {
                            type: String,
                            required: [true, "El nombre es requerido"],
                            alias: 'name'
                        }
                    },
                    alias: 'items',
                    _id: false
                }
            }
        ],
        alias: 'demographics',
        _id: false
    }
}, { timestamps: true, collection: 'lab21', versionKey: false });

export default model('Patient', PatientSchema);