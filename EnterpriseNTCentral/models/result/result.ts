import { model, Schema, Model } from 'mongoose';

const ResultSchema = new Schema<ResultDB>({
    lab22c1: {
        type: Number,
        required: [true, "La orden es requerida"],
        alias: 'order'
    },
    lab39: {
        type: {
            lab39c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab43: {
                type: {
                    lab43c1: {
                        type: Number,
                        required: [true, "El id es requerido"],
                        alias: 'id'
                    },
                    lab43c2: {
                        type: Number,
                        required: [true, "El ordenamiento es requerido"],
                        alias: 'ordering'
                    },
                    lab43c3: {
                        type: String,
                        required: [true, "La abreviatura es requerida"],
                        alias: 'abbreviature'
                    },
                    lab43c4: {
                        type: String,
                        required: [true, "El nombre es requerido"],
                        alias: 'name'
                    },
                    lab43c6: {
                        type: Number,
                        required: [true, "El tipo es requerido"],
                        alias: 'type'
                    },
                    lab43c9: {
                        type: String,
                        required: [true, "El nombre en ingles es requerido"],
                        alias: 'nameEnglish'
                    }
                },
                required: [true, "El area es requerido"],
                alias: 'area'
            },
            lab39c2: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            },
            lab39c3: {
                type: String,
                required: [true, "La abreviatura es requerida"],
                alias: 'abbreviature'
            },
            lab39c4: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab39c5: {
                type: Number,
                alias: 'complexityLevel'
            },
            lab39c6: {
                type: Number,
                alias: 'gender'
            },
            lab39c7: {
                type: Number,
                alias: 'minimumAge'
            },
            lab39c8: {
                type: Number,
                alias: 'maximumAge'
            },
            lab39c9: {
                type: Number,
                alias: 'unitOfYears'
            },
            lab39c11: {
                type: Number,
                alias: 'resultType'
            },
            lab39c12: {
                type: Number,
                alias: 'decimals'
            },
            lab39c19: {
                type: Number,
                required: [true, "Aplica a estadísticas es requerido"],
                alias: 'statistics'
            },
            lab39c20: {
                type: Number,
                required: [true, "Aplica a facturación es requerido"],
                alias: 'facturation'
            },
            lab39c21: {
                type: String,
                alias: 'statisticsTitle'
            },
            lab39c22: {
                type: Number,
                alias: 'multiplyBy'
            },
            lab39c23: {
                type: Number,
                required: [true, "Eliminar del perfil es requerido"],
                alias: 'removeFromProfile'
            },
            lab39c37: {
                type: Number,
                required: [true, "El tipo de prueba es requerido"],
                alias: 'type'
            }
        },
        required: [true, "El examen es requerido"],
        alias: 'test',
        _id: false
    },
    lab57c1: {
        type: String,
        required: [true, "El resultado es requerido"],
        alias: 'result'
    },
    lab57c2: {
        type: Date,
        required: [true, "La fecha del resultado es requerida"],
        alias: 'resultDate'
    },
    lab57c3: {
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
        required: [true, "El usuario de resultado es requerido"],
        alias: 'userResult',
        _id: false
    },
    lab57c4: {
        type: Date,
        required: [true, "La fecha de ingreso es requerida"],
        alias: 'entryDate'
    },
    lab57c5: {
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
        required: [true, "El usuario de ingreso es requerido"],
        alias: 'userEntry',
        _id: false
    },
    lab57c6: {
        type: String,
        alias: 'lastValidatedResult'
    },
    lab57c7: {
        type: Date,
        alias: 'dateLastValidatedResult'
    },
    lab57c8: {
        type: Number,
        required: [true, "El estado es requerido"],
        alias: 'status'
    },
    lab57c9: {
        type: Number,
        alias: 'pathology'
    },
    lab57c10: {
        type: Number,
        alias: 'isLocked'
    },
    lab57c11: {
        type: Date,
        alias: 'lastBlockDate'
    },
    lab57c12: {
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
        required: [true, "El usuario de ingreso es requerido"],
        alias: 'userEntry',
        _id: false
    },
    lab57c14: {
        type: {
            lab39c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab39c2: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            },
            lab39c3: {
                type: String,
                required: [true, "La abreviatura es requerida"],
                alias: 'abbreviature'
            },
            lab39c4: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            }
        },
        alias: 'profile',
        _id: false
    },
    lab57c15: {
        type: {
            lab39c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab39c2: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            },
            lab39c3: {
                type: String,
                required: [true, "La abreviatura es requerida"],
                alias: 'abbreviature'
            },
            lab39c4: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            }
        },
        alias: 'package',
        _id: false
    },
    lab57c16: {
        type: Number,
        required: [true, "El estado de la muestra es requerido"],
        alias: 'statusSample'
    },
    lab57c17: {
        type: Number,
        alias: 'optionalField'
    },
    lab57c18: {
        type: Date,
        alias: 'validationDate'
    },
    lab57c19: {
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
        alias: 'userValidation',
        _id: false
    },
    lab57c20: {
        type: Date,
        alias: 'datePrevalidation'
    },
    lab57c21: {
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
        alias: 'userPrevalidation',
        _id: false
    },
    lab57c22: {
        type: Date,
        alias: 'printDate'
    },
    lab57c23: {
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
        alias: 'userPrint',
        _id: false
    },
    lab45c2: {
        type: String,
        alias: 'unit'
    },
    lab64: {
        type: {
            lab64c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab64c2: {
                type: String,
                required: [true, "El codigo es requerido"],
                alias: 'code'
            },
            lab64c3: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            }
        },
        alias: 'technique',
        _id: false
    },
    lab48c1: {
        type: Number,
        alias: 'referenceValue'
    },
    lab57c24: {
        type: Number,
        alias: 'numberOfModifications'
    },
    lab57c25: {
        type: Number,
        alias: 'print'
    },
    lab57c26: {
        type: Number,
        alias: 'hasAntibiogram'
    },
    lab57c27: {
        type: Number,
        alias: 'minimumDelta'
    },
    lab57c28: {
        type: Number,
        alias: 'maximumDelta'
    },
    lab40c1: {
        type: {
            lab40c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab40c2: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            },
            lab40c3: {
                type: String,
                required: [true, "El name es requerido"],
                alias: 'name'
            }
        },
        required: [true, "El laboratorio es requerido"],
        alias: 'laboratory',
        _id: false
    },
    lab57c29: {
        type: Number,
        alias: 'printHistoryReport'
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
        alias: 'branch',
        _id: false
    },
    lab48c5: {
        type: Number,
        alias: 'minimalPanic'
    },
    lab48c6: {
        type: Number,
        alias: 'maximumPanic'
    },
    lab48c12: {
        type: Number,
        alias: 'normalMinimum'
    },
    lab48c13: {
        type: Number,
        alias: 'normalMaximum'
    },
    lab50c1_1: {
        type: Number,
        alias: 'panicLiteralResultId'
    },
    lab50c1_2: {
        type: String,
        alias: 'panicLiteralResult'
    },
    lab50c1_3: {
        type: Number,
        alias: 'normalLiteralResultId'
    },
    lab50c1_4: {
        type: String,
        alias: 'normalLiteralResult'
    },
    lab48c14: {
        type: Number,
        alias: 'minimumReportable'
    },
    lab48c15: {
        type: Number,
        alias: 'maximumReportable'
    },
    lab57c30: {
        type: String,
        alias: 'penultimateValidatedResult'
    },
    lab57c31: {
        type: Date,
        alias: 'datePenultimateValidatedResult'
    },
    lab57c32: {
        type: Number,
        alias: 'hasComment'
    },
    lab57c33: {
        type: Number,
        alias: 'numberOfRepetitions'
    },
    lab57c34: {
        type: Number,
        alias: 'verificationDateInt'
    },
    lab57c35: {
        type: Number,
        alias: 'typeOfIncomeResult'
    },
    lab57c36: {
        type: Number,
        alias: 'typeOfIncomeTest'
    },
    lab57c37: {
        type: Date,
        alias: 'verificationDate'
    },
    lab57c38: {
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
        alias: 'userVerification',
        _id: false
    },
    lab57c39: {
        type: Date,
        alias: 'takeDate'
    },
    lab57c40: {
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
        alias: 'userTake',
        _id: false
    },
    lab24c1: {
        type: {
            lab24c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab24c2: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab24c9: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            }
        },
        required: [true, "La muestra es requerida"],
        alias: 'sample',
        _id: false
    },
    lab24c1_1: {
        type: {
            lab24c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab24c2: {
                type: String,
                required: [true, "El nombre es requerido"],
                alias: 'name'
            },
            lab24c9: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            }
        },
        alias: 'subSample',
        _id: false
    },
    lab158c1: {
        type: Number,
        alias: 'anatomicalSite'
    },
    lab201c1: {
        type: Number,
        alias: 'collectionMethod'
    },
    lab57c41: {
        type: Number,
        alias: 'numberOfAttachments'
    },
    lab57c42: {
        type: Number,
        alias: 'hasTemplate'
    },
    lab57c43: {
        type: Number,
        alias: 'hasDelta'
    },
    lab57c44: {
        type: Date,
        alias: 'dateRetake'
    },
    lab57c45: {
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
        alias: 'userRetake',
        _id: false
    },
    lab57c46: {
        type: Date,
        alias: 'dateRepeat'
    },
    lab57c47: {
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
        alias: 'userRepeat',
        _id: false
    },
    lab57c48: {
        type: String,
        alias: 'lastRepeatedResult'
    },
    lab57c49: {
        type: String,
        alias: 'centralCode'
    },
    lab57c50: {
        type: Number,
        alias: 'sendHis'
    },
    lab57c51: {
        type: Date,
        alias: 'dateSendHis'
    },
    lab57c52: {
        type: Number,
        alias: 'pendingReason'
    },
    lab57c53: {
        type: Number,
        alias: 'dilution'
    },
    lab57c54: {
        type: Number,
        alias: 'remission'
    },
    lab57c55: {
        type: Date,
        alias: 'dateReportDoctor'
    },
    lab40c1a: {
        type: {
            lab40c1: {
                type: Number,
                required: [true, "El id es requerido"],
                alias: 'id'
            },
            lab40c2: {
                type: String,
                required: [true, "El código es requerido"],
                alias: 'code'
            },
            lab40c3: {
                type: String,
                required: [true, "El name es requerido"],
                alias: 'name'
            }
        },
        alias: 'laboratoryOrigin',
        _id: false
    },
    lab57c56: {
        type: Date,
        alias: 'Result'
    },
    lab57c57: {
        type: Date,
        alias: 'dateTransport'
    },
    lab57c58: {
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
        alias: 'userTransport',
        _id: false
    },
    lab57c59: {
        type: Date,
        alias: 'printSampleDate'
    },
    lab57c60: {
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
        alias: 'userPrintSample',
        _id: false
    },
    lab57c61: {
        type: Number,
        alias: 'hasInternalCommentResult'
    },
    lab57c62: {
        type: String,
        alias: 'modifiedResult'
    },
    lab57c63: {
        type: String,
        alias: 'printComment'
    },
    lab57c64: {
        type: Number,
        alias: 'verifiedMicrobiologyDestination'
    },
    lab57c65: {
        type: Number,
        alias: 'autoSend'
    },
    lab57c66: {
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
        alias: 'userPendingTest',
        _id: false
    },
    lab57c67: {
        type: String,
        alias: 'commentDeterminations'
    },
    lab57c68: {
        type: Number,
        alias: 'verifiedExternalLaboratoryDestination'
    },
    lab57c69: {
        type: Number,
        alias: 'ciade'
    },
    lab57c70: {
        type: String,
        alias: 'resultEnglish'
    },
    lab57c71: {
        type: String,
        alias: 'comment2'
    },
    lab57c72: {
        type: Number,
        alias: 'sendBoards'
    },
    lab57c73: {
        type: Number,
        alias: 'sendBoardsInCreation'
    },
    lab57c74: {
        type: Number,
        alias: 'central'
    },
    lab57c75: {
        type: Date,
        alias: 'dateCentral'
    },
    lab95:  {
        type: [String],
        alias: 'comments'
    },
    lab206: {
        type: [
            {
                lab76c1: {
                    type: Number,
                    required: [true, "El id es requerido"],
                    alias: 'id'
                },
                lab76c2: {
                    type: String,
                    required: [true, "El nombre es requerido"],
                    alias: 'name'
                },
                lab204c1: {
                    type: Number,
                    required: [true, "El id de la detección microbiana es requerido"],
                    alias: 'idMicrobialDetection'
                },
                lab204c2: {
                    type: String,
                    alias: 'comment'
                },
                lab204c4: {
                    type: String,
                    alias: 'recount'
                },
                lab204c6: {
                    type: String,
                    alias: 'complementations'
                },
                lab204: {
                    type:  [
                        {
                            lab77c1: {
                                type: Number,
                                alias: 'id'
                            },
                            lab79c1: {
                                type: Number,
                                alias: 'idAntibiotic'
                            },
                            lab79c2: {
                                type: String,
                                alias: 'nameAntibiotic'
                            },
                            lab78c2: {
                                type: Number,
                                alias: 'line'
                            },
                            lab45c1: {
                                type: String,
                                alias: 'unit'
                            },
                            lab205c1: {
                                type: String,
                                alias: 'cmi'
                            },
                            lab205c2: {
                                type: String,
                                alias: 'interpretationCMI'
                            },
                            lab205c3: {
                                type: String,
                                alias: 'cmiM'
                            },
                            lab205c4: {
                                type: String,
                                alias: 'interpretationCMIM'
                            },
                            lab205c5: {
                                type: Number,
                                alias: 'cmiMPrint'
                            },
                            lab205c6: {
                                type: String,
                                alias: 'disk'
                            },
                            lab205c7: {
                                type: String,
                                alias: 'interpretationDisk'
                            },
                            lab205c8: {
                                type: Number,
                                alias: 'diskPrint'
                            },
                            lab205c9: {
                                type: Date,
                                alias: 'dateCMI'
                            },
                            lab205c10: {
                                type: Date,
                                alias: 'dateCMIM'
                            },
                            lab205c11: {
                                type: Date,
                                alias: 'dateDisk'
                            }
                        }
                    ], 
                    alias: 'resultsMicrobiology',
                    _id: false
                }
            }
        ],
        alias: 'antibiogram',
        _id: false
    },
    lab58 : {
        type: [
            {
                lab51c1: {
                    type:  Number, 
                    required: [true, "El id es requerido"],
                    alias:  'id'
                },
                lab51c2: {
                    type:  String, 
                    required: [true, "La opción es requerida"],
                    alias:  'option'
                },
                lab51c3: {
                    type:  String,
                    alias:  'comment'
                },
                lab51c4: {
                    type:  Number, 
                    alias:  'order'
                },
                lab39c1: {
                    type:  Number, 
                    alias:  'test'
                },
                lab58c1: {
                    type:  String, 
                    alias:  'result'
                },
                lab58c3: {
                    type:  String, 
                    alias:  'normalResult'
                },
                lab58c4: {
                    type:  String,
                    alias:  'information'
                },
                lab146: {
                    type:  [
                        {
                            lab146c1: {
                                type: String,
                                alias: 'result'
                            },
                            lab146c2: {
                                type: Number, 
                                alias: 'sort'
                            },
                            lab146c3: {
                                type: Number, 
                                alias: 'reference'
                            },
                            lab146c4: {
                                type: String,
                                alias: 'comment'
                            }
                        }
                    ], 
                    alias: 'results',
                    _id: false
                }
            }
        ],
        alias: 'optionsTemplate',
        _id: false
    }
}, { timestamps: true, versionKey: false });

export default ResultSchema;