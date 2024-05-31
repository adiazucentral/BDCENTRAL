import { model, Schema } from 'mongoose';

const TemplateSchema = new Schema<TemplateBD>({
    lab35c2     : {
        type: String
    },
    lab35c3     : {
        type: String
    },
    lab35c4     : {
        type: String
    },
    lab35c5     : {
        type: String
    },
    lab35c6     : {
        type: String
    },
    lab35c7     : {
        type: String
    },
    lab35c8     : {
        type: Boolean
    },
    lab35c9     : {
        type: String
    },
    lab35c10    : {
        type: String
    },
    lab35c11    : {
        type: String
    },
    lab35c12    : {
        type: String
    },
}, { timestamps: true, collection: 'lab35', versionKey: false });

export default model('TemplateCollection', TemplateSchema);