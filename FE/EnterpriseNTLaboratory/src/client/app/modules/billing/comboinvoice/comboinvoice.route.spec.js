/* jshint -W117, -W030 */
describe('comboinvoice', function () {
    describe('state', function () {
        var view = 'app/modules/billing/comboinvoice/comboinvoice.html';

        beforeEach(function () {
            module('app.comboinvoice', bard.fakeToastr);
            bard.inject('$state');
        });

        it('should map state hematologicalcounter to url /comboinvoice ', function () {
            expect($state.href('comboinvoice', {})).to.equal('/comboinvoice');
        });
        it('should map /comboinvoice route to hematologicalcounter View template', function () {
            expect($state.get('comboinvoice').templateUrl).to.equal(view);
        });
    });
});