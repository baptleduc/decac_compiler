package fr.ensimag.deca.tree;

/**
 *
 * @author nicolmal
 * @date 13/01/2025
 */
public abstract class AbstractDeclField extends Tree {

    private Visibility vision;
    private AbstractIdentifier type;

    public void setType(AbstractIdentifier typeParam) {
        type = typeParam;
    }

    public AbstractIdentifier getType() {
        return type;
    }

    public void setVisibility(Visibility visibility) {
        vision = visibility;
    }

    public Visibility getVisibility() {
        return vision;
    }

}
