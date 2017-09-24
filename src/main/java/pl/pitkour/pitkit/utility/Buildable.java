package pl.pitkour.pitkit.utility;

public interface Buildable<T extends Builder>
{
	T toBuilder();
}
