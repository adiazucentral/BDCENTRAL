import { model, Schema } from 'mongoose';

const RemissionSchema = new Schema<RemissionDB>({
    lab22c1: {
        type: Number
    },
    lab39c1: {
        type: Number,
    },
    lab24c1: {
        type: Number,
    },
    lab05c1_1: {
        type: Number,
    },
    lab05c1_2: {
        type: Number,
    },
    lab57rc1: {
        type: Number
    }
}, { timestamps: true, collection: 'lab57r', versionKey: false });

export default model('RemissionCollection', RemissionSchema);

   