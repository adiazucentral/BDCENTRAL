import { model, Schema, Model } from 'mongoose';

const OrderSchema = new Schema<OrderBD>({
    lab22c1: {
        type: Number,
        unique : true,
        required: [true, "La orden es requerida"],
        alias: 'order'
    },
    lab22c2: {
        type: Number,
        required: [true, "La fecha de creación en Formato yyyymmdd es requerida"],
        alias: 'createdDateShort'
    },
    lab103: {
        type: {
            lab103c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab103c2: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            },
            lab103c3: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            }
        },
        alias: 'orderType'
    },
    lab22c3: {
        type: Date,
        required: [true, "La fecha de creación es requerida"],
        alias: 'createdDate'
    },
    lab21c1: {
        type: Number,
        required: [true, "El paciente es requerido"],
        alias: 'patientId'
    },
    lab22c4: {
        type: Number,
        alias: 'homebound'
    },
    lab22c5: {
        type: Number,
        alias: 'miles'
    },
    lab22c6: {
        type: Date,
        alias: 'lastUpdateDate'
    },
    lab04c1: {
        type: {
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
        required: [true, "El usuario de modificación es requerido"],
        alias: 'lastUpdateUser'
    },
    lab07c1: {
        type: Number,
        required: [true, "El estado de la orden es requerido"],
        alias: 'status'
    },
    lab22c7: {
        type: String,
        alias: 'externalId'
    },
    lab05: {
        type: {
            lab05c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab05c2: {
                type: String,
                required: [true, "La abreviatura es requerida"],
                alias: 'abbreviature'
            },
            lab05c4: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab05c10: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            }
        },
        required: [true, "La sede es requerida"],
        alias: 'branch'
    },
    lab10: {
        type: {
            lab10c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab10c2: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab10c7: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            }
        },
        alias: 'service'
    },
    lab19: {
        type: {
            lab19c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab19c2: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab19c3: {
                type: String,
                required: [true, "El apellido es requerido"],
                alias: 'lastname'
            },
            lab19c22: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            }
        },
        alias: 'physician'
    },
    lab14: {
        type: {
            lab14c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab14c2: {
                type: String,
                required: [true, "El nit es requerido"],
                alias: 'nit'
            },
            lab14c3: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab14c21: {
                type: String,
                required: [true, "El correo es requerido"],
                alias: 'email'
            }
        },
        alias: 'customer'
    },
    lab904: {
        type: {
            lab904c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab904c2: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            },
            lab904c3: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            }
        },
        alias: 'rate'
    },
    lab22c8: {
        type: Number,
        alias: 'totalTest'
    },
    lab22c9: {
        type: Number,
        alias: 'previousStatus'
    },
    lab22c10: {
        type: Number,
        alias: 'inconsistency'
    },
    lab22c11: {
        type: Number,
        alias: 'recallNumber'
    },
    lab22c12: {
        type: Number,
        alias: 'totalAttachments'
    },
    lab57c9: {
        type: Number,
        alias: 'pathology'
    },
    lab22c13: {
        type: String,
        alias: 'turn'
    },
    lab04c1_1: {
        type: {
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
        alias: 'createUser'
    },
    lab22c14: {
        type: Number,
        alias: 'LIH'
    },
    lab22c15: {
        type: Date,
        alias: 'attentionDate'
    },
    lab22c16: {
        type: String,
        alias: 'observations'
    },
    lab930c1: {
        type: Number,
        alias: 'comboBill'
    },
    lab22c17: {
        type: String,
        alias: 'configPrint'
    },
    lab22c18: {
        type: Number,
        alias: 'typeOfIncome'
    },
    lab22c19: {
        type: Number,
        alias: 'appointment'
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
                    }
                }
            }
        ]
    },
    lab60: {
        type:  [
            {
                lab60c1: {
                    type: Number,
                    required: [true, "El id es requerido"],
                    alias: 'id'
                },
                lab60c3: {
                    type: String,
                    required: [true, "El comentario es requerido"],
                    alias: 'comment'
                },
                lab60c4: {
                    type: String,
                    required: [true, "El tipo es requerido"],
                    alias: 'type'
                },
                lab60c6: {
                    type: Number,
                    required: [true, "El indicador si imprime es requerido"],
                    alias: 'isPrint'
                }
            }
        ]
    }
}, { timestamps: true, versionKey: false });

export default OrderSchema;